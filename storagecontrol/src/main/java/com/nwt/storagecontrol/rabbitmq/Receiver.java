package com.nwt.storagecontrol.rabbitmq;

import com.nwt.storagecontrol.model.SkladisneJedinice;
import com.nwt.storagecontrol.model.Skladiste;
import com.nwt.storagecontrol.repos.SkladJedRepository;
import com.nwt.storagecontrol.repos.SkladisteRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;


@RabbitListener(queues = "billings-queue")
public class Receiver {

    @Autowired
    SkladJedRepository skladJedRepository;
    @Autowired
    private AmqpTemplate amqpTemplate;

    private String EXCHANGE = "storage-exchange";
    private String ROUTING_KEY = "storage";

    @RabbitHandler
    public void receive(String deleteBilling) {
        System.out.println(" [x] Received '" + deleteBilling + "'");
        if(deleteBilling.charAt(0) == 'F')
        {
            SkladisneJedinice skladJed = skladJedRepository.findById(Long.parseLong(deleteBilling.substring(1))).get();
            skladJed.setObrisan(Boolean.FALSE);
            skladJedRepository.save(skladJed);
        }
        else if(deleteBilling.charAt(0) == 'S')
        {
            try
            {
                skladJedRepository.deleteById(Long.parseLong(deleteBilling.substring(1)));
                String delBilling = "S" + deleteBilling.substring(1) ;
                amqpTemplate.convertAndSend(EXCHANGE, ROUTING_KEY,delBilling);
            }
            catch (Exception e)
            {
                System.out.println(" Error: '" + e + "'");
                String delBilling = "F" + deleteBilling.substring(1) ;
                amqpTemplate.convertAndSend(EXCHANGE, ROUTING_KEY,delBilling);
            }
        }
    }
}
