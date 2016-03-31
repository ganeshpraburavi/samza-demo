package in.ganesh.samzademo.task

import java.util.Properties

import org.apache.kafka.clients.producer.{RecordMetadata, Callback, ProducerRecord, KafkaProducer}
import org.apache.kafka.common.serialization.StringSerializer

object MessageLoader extends App {
  lazy val kafkaProducer = {
    val broker = "localhost:9092"
    val props = new Properties()
    props.put("bootstrap.servers", broker)
    props.put("producer.type", "sync")
    new KafkaProducer[String, String](props, new StringSerializer, new StringSerializer)
  }

  val topic = "Comments"

  val message = List(new ProducerRecord[String, String](topic, "Tester", "fcuk"), new ProducerRecord[String, String](topic, "Teste", "Good Message"))
  var i = 0
  while (i < 1000) {
    kafkaProducer.send(message(i % 2), new Callback() {
      override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit = {
        if (exception != null)
          println(exception)
        else {
          println(s"Written message")
        }
      }
    })
    i += 1
  }
  kafkaProducer.close()
}
