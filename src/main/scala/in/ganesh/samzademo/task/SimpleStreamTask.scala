package in.ganesh.samzademo.task

import org.apache.samza.config.Config
import org.apache.samza.system.{IncomingMessageEnvelope, OutgoingMessageEnvelope, SystemStream}
import org.apache.samza.task._
import org.slf4j.LoggerFactory


class SimpleStreamTask extends StreamTask with InitableTask with ClosableTask with WindowableTask {

  val log = LoggerFactory.getLogger(classOf[SimpleStreamTask])

  val abusiveWords = scala.collection.mutable.Set[String]()
  var abusiveCommentsTopic = ""
  var goodCommentsTopic = ""

  override def init(config: Config, context: TaskContext): Unit = {
    // config object will have all the properties in the property file
    val fileName = config.get("filepath.badword")
    abusiveCommentsTopic = config.get("topic.abusivecomments")
    goodCommentsTopic = config.get("topic.goodcomments")
    val stream = getClass.getResourceAsStream(s"/$fileName")
    for (word <- scala.io.Source.fromInputStream(stream).getLines) abusiveWords += word
  }

  override def process(envelope: IncomingMessageEnvelope, collector: MessageCollector, coordinator: TaskCoordinator): Unit = {
    // Process each message
    val user = envelope.getKey.toString
    val msg = envelope.getMessage.toString
    log.info(s"Got Message => User = $user, Message = $msg")
    if (isAbusiveMessage(msg)) collector.send(KafkaMessage(abusiveCommentsTopic, user, msg).outgoingMessageEnvelope)
    else collector.send(KafkaMessage(goodCommentsTopic, user, msg).outgoingMessageEnvelope)
  }

  override def close(): Unit = {
    // close any holding resource/open file handles
    log.info("Close Method: Task got close signal. Closing Task")
  }

  override def window(collector: MessageCollector, coordinator: TaskCoordinator): Unit = {
    log.info("Window Method: Commiting/Checkpointing offset")
    // called every task.window.ms
    coordinator.commit(TaskCoordinator.RequestScope.CURRENT_TASK)
  }

  def isAbusiveMessage(msg: String) = abusiveWords.exists(msg.contains)
}

case class KafkaMessage(topic: String, key: String, message: String) {
  val outgoingMessageEnvelope = new OutgoingMessageEnvelope(new SystemStream("kafka", topic), key, message)
}