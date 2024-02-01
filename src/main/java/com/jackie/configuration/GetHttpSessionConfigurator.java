package com.jackie.configuration;

import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

@Configuration
public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator{
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response){
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        String cookies = request.getHeaders().get("cookie").get(0);
        String token = cookies.substring(cookies.indexOf("token=") + 6, cookies.indexOf(';', cookies.indexOf("token=") + 1) == -1 ? cookies.length() : cookies.indexOf(';', cookies.indexOf("token=") + 1));
        httpSession.setAttribute("token",token);
        sec.getUserProperties().put(HttpSession.class.getName(),httpSession);
    }
}
