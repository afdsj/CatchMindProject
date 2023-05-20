package client;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import gui.GameRoomFrame;
import utils.Protocol;

public class MainClient {

	// TAG : 문자열 상수이고 MainClient라는 값을 가지고 있다
	private static final String TAG = "MainClient : ";

	/* 클래스의 객체를 참조하는 변수 
	 * gameroomFrame : 게임 방 프레임
	 * socket : 클라이언트와 서버 간의 소켓 연결 관리
	 * bw : 소켓의 출력 스트림과 연결되어 데이터를 서버로 전송하는데 사용
	 * keyboardln : 소켓의 입력 스트림과 연결되어 서버로부터 데이터를 읽어오는데 사용
	 * bi : 이미지 데이터를 저장하는데 사용
	 * */
	private GameRoomFrame gameroomFrame; 
	Socket socket; 
	BufferedWriter bw;	
	BufferedReader keyboardln;
	BufferedImage bi;  

	// MainClient 클래스의 생성자, GameRoomFrame 매개변수
	public MainClient(GameRoomFrame gameroomFrame) {
		this.gameroomFrame = gameroomFrame; // gameroomFrame 멤버변수 =gameroomFrame 객체를 참조
		try { //예외 발생
			socket = new Socket("localhost", 8892); //socket 변수에 localhost주소와 포트번호 8892를 가지는 소켓 생성
			ReadThread rt = new ReadThread(); // rt객체 생성, 서버로부터 메세지를 읽고 처리하는 역활
			Thread newWorker = new Thread(rt); //rt를 이용해서 스레드 newWorker 생성
			newWorker.start(); // 스레드를 시작하는 부분
			// bw 변수에 socket 출력스트림을 BufferWriter로 객체 생성, bw를 통해서 데이터를 서버로 전송
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			// keyboardln 변수 socket 입력스트림을 BufferReader로 객세 생성, keyboardln를 통해서 서버로부터 데이터 읽어오기
			keyboardln = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

		} catch (Exception e) { //예외 처리
			e.printStackTrace();
		}
	}

	/* 스타트 버튼 클릭 : StartGame, Chat:안녕
	 * send메소드, 서버로 메세지를 전송하는 역할
	 * outputMsg 문자열 형태의 매개변수 */
	public void send(String outputMsg) {
		try {
			bw.write(outputMsg + "\n"); //outputMsg문자열을 bw를 사용하여 서버로 전송 후 줄바꿈
			System.out.println(TAG + "send 확인!!!!!!!!!!!!" + outputMsg); // TAG와 outputMsg 결합
			bw.flush();	// 버퍼에 남아있는 데이터를 모두 서버로 전송, 버퍼를 비우는 작업이므로 필요한 경우에 호출한다
		} catch (IOException e) { //write, flush메소드 호출 중에 예외 발생하면 예외 처리하는 블록
			e.printStackTrace();
		}
	}
	/* ReadThread 클래스는 Runnable 인터페이스를 구현하여 스레드로 동작 
	 * 서버로부터 데이터를 읽고 처리하는 역할을 수행*/
	class ReadThread implements Runnable {

		public int x; // int 타입의 필드 : x
		public int y; // int 타입의 필드 : y

		@Override
		public void run() { //Runnable 스레드가 동작하는 부분의 run메소드
			try {
				// socket.getInputStream()을 사용하여 소켓으로부터 데이터를 읽을 수 있는 br 생성
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				String inputMsg = ""; //inputMsg 문자열 변수 선언 후 ""로 초기화
				// br.readLine()메소드 호출 -> 한 줄씩 데이터를 읽으며 데이터가 없을때까지 반복
				while ((inputMsg = br.readLine()) != null) { 
					/* inputMsg 변수에 읽은 데이터 저장 , 메소드 호출 후 읽은 데이터 처리 
					 * router메소드는 inputMsg를 인자로 받아 서버로부터 받은 데이터에 대한 처리를 수행*/
					router(inputMsg); 

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// router 메소드 , 서버로부터 받은 메세지를 분석하고 처리하는 역할
		public void router(String msgLine) {
			// 만약에 Chat
			
			/* msgLine : 매개변수는 서버로부터 받은 메세지를 나타낸다
			 * msgLine.split(":") msgLine 문자열을 : 기준으로 분한하여 문자열 배열로 반환
			 * 반환된 배열은 'msg'변수에 저장
			 * */
			String[] msg = msgLine.split(":");
			String protocol = msg[0]; // protocol 변수, msg 1번째 인덱스
			if (protocol.equals(Protocol.CHAT)) { // 프로토콜이 chat인 경우 
				String username = msg[1]; // 2번째 인덱스는 username (사용자 이름)
				String chatMsg = msg[2]; // 3째 인덱스는 chatMsg (채팅 메세지)
				/* [사용자 이름]메세지 형식으로 채팅 메세지 생성하고 taChat(채팅창)에 추가 
				 * 새로운 채팅 메세지는 줄 바꾸고 채팅창의 맨 아래에 추가*/
				gameroomFrame.taChat.append(" [ " + username + " ] " + chatMsg + "\n");
				// 채팅창의 스크롤 위치를 맨 아래로 이동
				gameroomFrame.taChat.setCaretPosition(gameroomFrame.taChat.getDocument().getLength());

			} else if (protocol.equals(Protocol.STARTGAME)) { // protocol이 STARTGAME인 경우
				// 만약에 제시어:false -> 그림판 비활성화, 채팅창 활성화, 제시어부분 클리어, 그림판 초기화, 게임시작버튼 비활성화
				// 만약에 제시어:다른게 -> 그림판 활성화, 채팅창 비활성화, 제시어부분 넣어주기, 그림판 초기화, 게임시작버튼 비활성화
				String chatMsg = msg[1];
				if (chatMsg.equals("false")) {
					gameroomFrame.can.setEnabled(false); 
					gameroomFrame.tfChat.setEnabled(true);
					gameroomFrame.tfCard.setText("");
					gameroomFrame.can.getGraphics().clearRect(0, 0, 900, 900);
					gameroomFrame.btGstart.setEnabled(false);
				} else { // 제시어 턴의 주인
					gameroomFrame.can.setEnabled(true);
					gameroomFrame.tfChat.setEnabled(false);
					gameroomFrame.tfCard.setText(chatMsg);
					gameroomFrame.can.getGraphics().clearRect(0, 0, 900, 900);
					gameroomFrame.btGstart.setEnabled(false);
				}
			} else if (protocol.equals(Protocol.DRAW)) { // protocol이 DRAW 인 경우
				String[] drawMsg = msg[1].split(","); // 2번째 인덱스 : msg ,를 기준으로 분리 후 drawMsg 배열에 저장
				x = Integer.parseInt(drawMsg[1]); // drawMsg [1] 인덱스에 저장된 문자열을 정수로 변환 후 x 변수에 할당
				y = Integer.parseInt(drawMsg[2]);
				System.out.println(TAG + "drawMsg : x : " + x + ",  y : " + y);

				gameroomFrame.setColor(drawMsg[0]); // setColor 메소드 호출하여 그림 그리기 색상 설정
				gameroomFrame.can.setX(x); // 그림판 x좌표 설정
				gameroomFrame.can.setY(y); // 그림판 y좌표 설정
				gameroomFrame.can.repaint(); // 그림판 다시 그리기
			} else if (protocol.equals(Protocol.ENDGAME)) { // protocol이 ENDGAME인 경우
				gameroomFrame.taChat.setText("게임이 종료되었습니다."); // 텍스트 설정
				gameroomFrame.btGstart.setEnabled(true); // 게임 시작 버튼 활성화로 변경
			} else if (protocol.equals(Protocol.CONNECT)) { // Protocol이 CONNECT인 경우
				// msg[0] : 프로토콜, msg[1] : getUserListParser() - userList + ","
				String[] connectMsg = msg[1].split(",");
				gameroomFrame.taUserList.setText(""); // 사용자 목록을 나타내는 텍스트 영역인 유저리스트 초기화
				for (int i = 0; i < connectMsg.length; i++) {
					gameroomFrame.taUserList.append(connectMsg[i] + "\n"); //
					System.out.println(TAG + "connect 확인 : " + connectMsg[i]);
				}

			} else if (protocol.equals(Protocol.ALLERASER)) { // Protocol이 ALLERASER인 경우
				gameroomFrame.can.getGraphics().clearRect(0, 0, 900, 900); // 그림판 전체 지우기

			}
		}
	}
}