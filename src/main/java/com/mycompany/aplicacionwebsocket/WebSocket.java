/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplicacionwebsocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author rjsaa
 */
@ServerEndpoint("/websocketend")
public class WebSocket {
    
    private static Set<Session> clientes = Collections.synchronizedSet(new HashSet<Session>());
    
    @OnOpen
    public void open(Session session){
        clientes.add(session);
    }
    
    @OnClose
    public void close(Session session){
        clientes.remove(session);
    }
    
    @OnError
    public void error(Throwable error) {
        error.printStackTrace();
    }
    
    @OnMessage
    public void message(String message, Session session){
        
        if(message.contains("&")){
            String[] split = message.split("&");
            String echoMsg = "client "+session.getId() + ": "+ split[0];
            
            synchronized (clientes) {
            for(Session client: clientes){
                
                if(client.getId().equalsIgnoreCase(split[1])){
                    try {
                       client.getBasicRemote().sendText(echoMsg);
                    }catch(IOException e){
                    
                    }
                }
    
            }
            }
            
        }else{
        String echoMsg = "client "+session.getId() + ": "+ message;
        
        synchronized (clientes) {
            for(Session client: clientes){
                try {
                    client.getBasicRemote().sendText(echoMsg);
                }catch(IOException e){
                    
                }
            }
        }
        
        }
    }
}
