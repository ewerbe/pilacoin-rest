package br.ufsm.poli.csi.tapw.pilacoin.server.colherdecha;

import br.ufsm.poli.csi.tapw.pilacoin.model.PilaCoin;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Logger;

import static java.math.BigInteger.valueOf;
@JsonPropertyOrder(alphabetic = true)
@Service
public class WebSocketClient {

    private static MyStompSessionHandler sessionHandler = new MyStompSessionHandler();
    @Value("${endereco.server}")
    private String enderecoServer;

    @PostConstruct
    private void init() {
        System.out.println("iniciou");
        StandardWebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.connect("ws://" + enderecoServer + "/websocket/websocket", sessionHandler);
        System.out.println("conectou");
    }

    @Scheduled(fixedRate = 3000)
    private void printDificuldade() {
        if (sessionHandler.dificuldade != null) {
            System.out.println("Dificuldade Atual --- printDificuldade: " + sessionHandler.dificuldade);
            inicio();
        }
    }

//    private static String getDificuldade() {
//        String dificuldade = String.valueOf(sessionHandler.dificuldade);
//        return dificuldade;
//    }

    private static class MyStompSessionHandler implements StompSessionHandler {

        private BigInteger dificuldade;

        @Override
        public void afterConnected(StompSession stompSession,
                                   StompHeaders stompHeaders)
        {
            stompSession.subscribe("/topic/dificuldade", this);
        }

        @Override
        public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {
        }

        @Override
        public void handleTransportError(StompSession stompSession, Throwable throwable) {
        }

        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            //System.out.println(stompHeaders);
            if (Objects.equals(stompHeaders.getDestination(), "/topic/dificuldade")) {
                return DificuldadeRet.class;
            }
            return null;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            //System.out.println("Received : " + o);
            assert o != null;
            dificuldade = new BigInteger(((DificuldadeRet) o).getDificuldade(), 16);
        }
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DificuldadeRet {
        private String dificuldade;
    }

//    public String mineradorPilaCoin (String dififuldade) {
//
//        return null;
//    }


    //////////////////////////////////////

    public static KeyPair kp;
@SneakyThrows
private static void inicio() {
    KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
    kpg.initialize(2048);
    kp = kpg.generateKeyPair();

    new Thread(t1).start();
//    new Thread(t1).start();
//    new Thread(t1).start();
//    new Thread(t1).start();
//    new Thread(t1).start();
}




    private static Runnable t1 = new Runnable() {
        public void run() {
            try{
                miner(
                );
            } catch (Exception e){
                System.out.println("ERRO AO CHAMAR O miner()");
            }
        }
    };


    @SneakyThrows
    private static void miner(){
        BigInteger numTentativas = valueOf(0);


        while (true) {

            SecureRandom sr = new SecureRandom();

            PilaCoin pilaCoin = PilaCoin.builder()
                    .dataCriacao(new Date())
                    .chaveCriador(kp.getPublic().getEncoded())
                    .nonce(new BigInteger(128, sr))
                    .idCriador("Ewerton PilaCoin").build();

            String pilaJson = new ObjectMapper().writeValueAsString(pilaCoin);

            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] hash = md.digest(pilaJson.getBytes("UTF-8"));

            BigInteger numHash = new BigInteger(hash).abs();

            if(numHash.compareTo(sessionHandler.dificuldade) < 0){
//            if (numHash.compareTo(BigInteger.valueOf(Long.parseLong(dificuldade))) < 0) {
                System.out.println("************************************** MINEROU PRA CARAI!**");
                System.out.println("Numero de tentativas = " + numTentativas);
                //TODO: mandar o pilaJson
                //registraPilaCoin(pilaCoin);
            } else {
                //System.out.println("Não MINEROU! ******* AINDA!");
                numTentativas =  numTentativas.add(BigInteger.ONE);
            }
        }
    }


    //TODO: método pra registrar o pila no server
    public void registraPilaCoin(PilaCoin pilaCoin) {
        //TODO: parou aqui
    }
}
