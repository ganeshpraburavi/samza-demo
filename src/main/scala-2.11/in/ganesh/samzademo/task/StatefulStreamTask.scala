package in.ganesh.samzademo.task

import org.apache.samza.config.Config
import org.apache.samza.storage.kv.RocksDbKeyValueStore
import org.apache.samza.system.{IncomingMessageEnvelope, OutgoingMessageEnvelope, SystemStream}
import org.apache.samza.task._
import org.slf4j.LoggerFactory

class StatefulStreamTask extends StreamTask with InitableTask with ClosableTask with WindowableTask {

  val log = LoggerFactory.getLogger(classOf[SimpleStreamTask])

  RocksDbKeyValueStore


  override def init(config: Config, context: TaskContext): Unit = {
  }

  override def process(envelope: IncomingMessageEnvelope, collector: MessageCollector, coordinator: TaskCoordinator): Unit = {
  }

  override def close(): Unit = {
  }

  override def window(collector: MessageCollector, coordinator: TaskCoordinator): Unit = {
  }
}

case class KafkaMessage(topic: String, key: String, message: String) {
  val outgoingMessageEnvelope = new OutgoingMessageEnvelope(new SystemStream("kafka", topic), key, message)
}