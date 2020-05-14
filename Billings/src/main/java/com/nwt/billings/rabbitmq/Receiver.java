package com.nwt.billings.rabbitmq;

import com.nwt.billings.model.Zakupnina;
import com.nwt.billings.repos.ZakupninaRepo;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@RabbitListener(queues = "storage-queue")
public class Receiver {
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private ZakupninaRepo zakupninaRepo;
    private String EXCHANGE = "billings-exchange";
    private String ROUTING_KEY = "billings";

    @RabbitHandler
    public void receive(String deleteBilling) {
        System.out.println(" [x] Received '" + deleteBilling + "'");


        if(deleteBilling.charAt(0) == 'B')
        {
            List<Zakupnina> zakupnine  = zakupninaRepo.findByJedinicaId(Long.parseLong(deleteBilling.substring(1)));
            if(zakupnine.size() == 1)
            {
                try
                {
                    Zakupnina zakupnina = zakupnine.get(0);
                    zakupnina.setObrisan(Boolean.TRUE);
                    zakupninaRepo.save(zakupnina);
                }
                catch (Exception e)
                {
                    String delBilling = "F" + deleteBilling.substring(1) ;
                    amqpTemplate.convertAndSend(EXCHANGE, ROUTING_KEY,delBilling);
                    return;
                }
            }
            String delBilling = "S" + deleteBilling.substring(1) ;
            amqpTemplate.convertAndSend(EXCHANGE, ROUTING_KEY,delBilling);
        }
        else if(deleteBilling.charAt(0) == 'F')
        {
            List<Zakupnina> zakupnine  = zakupninaRepo.findByJedinicaId(Long.parseLong(deleteBilling.substring(1)));
            if(zakupnine.size() == 1)
            {
                Zakupnina zakupnina = zakupnine.get(0);
                zakupnina.setObrisan(Boolean.FALSE);
                zakupninaRepo.save(zakupnina);
            }
        }
        else if(deleteBilling.charAt(0) == 'S')
        {
            List<Zakupnina> zakupnine  = zakupninaRepo.findByJedinicaId(Long.parseLong(deleteBilling.substring(1)));
            if(zakupnine.size() == 1)
            {
                zakupninaRepo.deleteById(zakupnine.get(0).getId());
            }
        }


    }
}
