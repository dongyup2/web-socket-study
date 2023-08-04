package websocket;

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

@ServerEndpoint("/ChatingServer") // 웹소켓 서버의 요청명을 설정
public class ChatServer {
	private static Set<Session> clients = 
			Collections.synchronizedSet(new HashSet<Session>());
	
	@OnOpen // 클라이언트가 접속했을 때 요청되는 메서드 정의
	public void onOpen(Session session) {
		clients.add(session);
		System.out.println("웹소켓 연결 : " + session.getId());
	}
	
	@OnMessage // 클라이언트로부터 메시지가 전송되었을 때 실행되는 메서드 정의
	public void onMessage(String message, Session session) throws IOException {
		System.out.println("메시지 전송 : " + session.getId() + ": " + message);
		synchronized(clients) {
			for(Session client : clients) {
				if(!client.equals(session)) {
					client.getBasicRemote().sendText(message);
				}
			}
		}
	}
	
	@OnClose // 클라이언트의 접속이 종료되면 실행되는 메서드 정의
	public void onClose(Session session) {
		clients.remove(session);
		System.out.println("웹소켓 종료 : " + session.getId());
	}
	
	@OnError// 에러 발생 시 실행되는 메서드 정의
	public void onError(Throwable e) {
		System.out.println("에러 발생");
		e.printStackTrace();
	}
	
}
