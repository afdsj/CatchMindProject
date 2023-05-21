package server;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import Paint.MyCanvas;
import gui.CountDown;
import gui.GameRoomFrame;
import utils.Protocol;

public class Mainserver {
	
	 //userList : 새로운 사용자가 추가되거나 기존 사용자가 제거될 때 변경사항 반영,
	public ArrayList<String> userList = new ArrayList<>();
	private final static String TAG = "MainServer : "; // 상수 필드
	
	ServerSocket serverSocket; // 서버프로그램에서 사용하는 소켓으로 ServerSocket 객체 생성하여 클라이언트가 연결해오는것을 기다림
	public static Vector<SocketThread> vc; // 변수 : vc, Vector에 저장되는 요소의 타입을 SocketThread 클래스로 지정하는 제네릭 
	String trunWord = null; // 변수 : trunWord, 게임 진행 중에 사용자의 입력된 단어나 턴에 대한 정보를 저장하는 역할
	int turn = 0;  // 변수 : turn,  게임의 현재 턴을 나타내는 역할
	public boolean 정답; // 변수 : 정답, 게임의 정답 여부를 나타내는 역할
	private GameRoomFrame gameRoomFrame; // 변수 : gameRoomFrame
	private int allTurn = 2; // 변수 : allTurn, 게임의 턴 수를 나타내는 역할
	private int currentTurn = 0; /* 변수 : currenTurn, 현재 진행 중인 턴을 나타내는 역할
									allTurn변수와 비교하여 게임의 턴 진행을 제어하는데 사용*/	
	public Mainserver() throws Exception{ // 생성자는 클래스가 인스턴스화 될 때 실행되는 메소드
		vc = new Vector<>(); // vc 변수를 Vector 클래스의 객체로 초기화
		serverSocket = new ServerSocket(8892); /* severSocket 변수를 ServerSocket 클래스의 객체로 초기화
												  ServerSocket은 8892 포트 번호를 사용하여 서버 소켓 생성
												  서버는 해당 포트에서 클라이언트의 연결을 수락 할 수 있다*/
		System.out.println(TAG + "서버접속완료"); // MainServer : 서버접속완료라는 메세지를 출력
		
		// 클라이언트의 접속 요청을 수락하고 처리하는 부분
		while(true) {
			Socket socket = serverSocket.accept(); /* serverSocket 객체를 사용하여 클라이언트의 접속 요청을 수락
													  accept() 메소드는 클라이언트의 연결 요청이 올 때까지 대기하다가
													  요청이 도착하면 해당 클라이언트와의 통신을 위한 소켓 객체 반환*/
			System.out.println(TAG + "접속 요청이 들어왔습니다."); // MainServer : 접속 요청이 들어왔습니다
			SocketThread st = new SocketThread(socket); /* SocketThread 클래스의 객체 생성, socket 초기화, 
														   클라이언트와의 통신을 담당하는 클래스*/
			Thread newWorker = new Thread(st); // st 객체를 이용하여 새로운 Thread 객체 생성
			newWorker.start(); /* 새로 생성된 Thread 시작, 
			 					  start()메소드를 호출하면 SocketThread 클래스에서 오버라이딩한 
			 					  run() 메서드 코드가 실행*/
			vc.add(st); /* vc라는 Vector 객체에 st 추가
			 			   새로운 클라이언트와 통신을 담당하는 SocketThread 객체를 vc에 저장
			 			   이를 통해 서버는 여러 클라이언트와 동시에 통신 가능*/
		}
	}
	
	// SocketThread 클래스가 Runnable 인터페이스를 구현하면 해당 크래스의 객체를 스레드로 실행 가능
	class SocketThread implements Runnable{
		
		SocketThread socketThread = this; // SocketThread 클래스의 현재 객체를 socketThread 변수에 할당, 나중에 참고하기 위한 용도로 사용
		Socket socket; // Socket 클래스의 socket 변수 선언, 클라이언트와의 통신을 위한 소켓 객체를 저장할 용도로 사용
		BufferedReader br; // BufferedReader 클래스의 br 변수 선언, 입력스트림을 생성하여 클라이언트로부터 데이터를 읽을 때 사용
		BufferedWriter bw; // BufferedWriter 클래스의 bw 변수 선언, 출력스트림을 생성하여 클라이언트로부터 데이터를 전송할 때 사용
		String username; // 문자열 타입의 username 변수 선언, 클라이언트의 사용자 이름을 저장하는 용도로 사용
		
		public BufferedImage bi; // BufferedIamge클래스의 객체 bi, BufferedImage : 이미지 데이터를 저장할 용도로 사용
		public MyCanvas myCanvas; // MyCanvas클래스의 객체 myCanvas, MyCanvas : 그래픽 요소를 그리고 조작하는 용도로 사용
		public int x; // 변수 x
		public int y; // 변수 y
		private String turnWord; // 변수 turnWord, 턴에 대한 정보다 단어를 저장, SocketTHREAD 클래스 내부에서만 접근 가능
		
		public SocketThread(Socket socket) { //SocketThread 클래스의 매개변수  Socket, 클라이언트와의 통신에 사용되는 소켓을 가지게 된다
			this.socket = socket; /* 생성자의 매개변수로 전달받은 Socket 객체를 멤버 변수인 socket에 할당하는 역할
			 						 socket 멤버변수를 통해 클라이언트와의 통신에 사용할 수 있게 된다*/
		}

		@Override
		public void run() {
			try {
				br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				/* socket.getInputStream() : 클라이언트로부터 데이터를 읽어오는 용도로 사용
				 * InputStreamReader클래스를 사용하여 입력스트림을 문자로 변환
				 * 문자 인코딩 방식인 UTF-8 사용해 받은 데이터를 문자열로 해석*/
				bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
				/* socket.getOutputStream() : 클라이언트로 데이터를 전송하는 용도로 사용
				 * OutputStreamWriter클래스를 사용하여 출력스트림을 문자로 변환
				 * 문자 인코딩 방식인 UTF-8 사용해 받은 데이터를 전송할 때 문자열로 인코딩*/
				
				String msg = ""; // 변수 msg, 클라이언트로부터 받은 메세지를 저장할 
				while ((msg = br.readLine()) != null) { /* 클라이언트로부터 한 줄씩 데이터를 읽어 msg 변수에 할당
				 										   클라이언트와의 연결이 유지되어 있는 동안 계속해서 반복*/
					System.out.println(TAG + "클라이언트 : " + msg); // 읽어온 클라이언트 메세지를 출력
					router(msg); // 읽어온 클라이언트 메세지를 router()메소드에 전달하여 처리
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		// 클라이언트로부터 받은 메세지를 분석하여 프로토콜에 따라 적절한 동작을 수행하는 역할
		private void router(String msgLine) { // 문자열 매개변수 msgLine
			
			String[]msg = msgLine.split(":"); /* 'msgLine'을 콜론':'을 기준으로 분리하여 문자열 배열 msg에 저장
			 									 split(":")메소드는 msgLine문자열을 콜론 기준으로 나누고,
			 									 그 결과를 배열로 반환*/
			String protocol = msg[0]; // msg 배열의 첫 번째 요소인 프로토콜 문자열이 저장
			
			if(protocol.equals(Protocol.CHAT)) { /* protocol 값과 Protocol.CHAT 상수의 값이 동일한지 비교하는 조건
			 										equals()메소드는 두 문자열이 동일한지 비교하여 참/거짓 반환*/
				String username = msg[1]; // msg 두번째 인덱스 username을 String 변수에 저장
				String chatMsg = msg[2]; // msg 세번째 인덱스 username을 String 변수에 저장
				System.out.println(TAG + "현재 제시어 :" + turnWord); /* TAG 문자열 변수로 가정, turnWord는 또 다른 변수로 가정
				 													  문자열 연결연산자를 사용해서 문자열합치기*/
				System.out.println(TAG + "chatMsg : " + chatMsg);/* TAG 문자열 변수로 가정, chatMsg는 또 다른 변수로 가정
				  													문자열 연결연산자를 사용해서 문자열합치기*/
			
				if(chatMsg.equals(turnWord)) { /* chatMsg(채팅메세지)와 turnWord(제시어) 값이 동일한지 비교하는 조건
												  equals()메소드는 두 문자열이 동일한지 비교하여 참/거짓 반환*/
					System.out.println(TAG + "정답 : " + chatMsg + "turnWord : " + turnWord);
					chattingMsg(username + ":" + chatMsg); // username : chatMsg 문자열 생성 후 chattingMsg메소드 전달
					nextTurn(); // 다음 턴 수행하기
				}else { // 채팅메세지와 제시어 동일하지 않을 경우
					System.out.println(TAG + "메시지 : " + chatMsg + "turnWord : " + turnWord);
					chattingMsg(username + ":" + chatMsg); 
				}
			}else if(protocol.equals(Protocol.STARTGAME)) { /* protocol 값과 Protocol.STARTGAME 상수의 값이 동일한지 비교하는 조건
															   equals()메소드는 두 문자열이 동일한지 비교하여 참/거짓 반환*/
				startGame();
			}else if(protocol.equals(Protocol.DRAW)) { /* protocol 값과 Protocol.DRAW 값이 동일한지 비교하는 조건
				   										  equals()메소드는 두 문자열이 동일한지 비교하여 참/거짓 반환*/
				//System.out.println(TAG + msg[1] + "DRAW 프로토콜 확인);
				
				// 예외처리
				try {
					for(SocketThread socketThread : vc) {
						if(socketThread != this) { 
							socketThread.bw.write(Protocol.DRAW + ":" + msg[1] + "\n");
							socketThread.bw.flush();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(protocol.equals(Protocol.CONNECT)) { /* protocol 값과 Protocol.CONNECT 값이 동일한지 비교하는 조건
					  									    equals()메소드는 두 문자열이 동일한지 비교하여 참/거짓 반환*/
				//username - msg[1]
				
				userList.add(msg[1]); //msg 두번째 인덱스 값을 userList에 추가
				
				System.out.println(TAG + "userList 확인 : " + userList);
				
				/* userList는 배열이라서 바로 보내는 것이 불가능
				   getuserListParse를 만들어서 배열을 string으로 풀어서 보냄 */
				System.out.println(TAG + "vc.size() 확인!!" + vc.size()); //vc리스트의 크기 출력
				try {
					for(SocketThread socketThread : vc) {
						if(true) {
							socketThread.bw.write(Protocol.CONNECT + ":" + getUserListParse() + "\n");
							System.out.println(TAG + "check userList 확인 : " + getUserListParse());
							socketThread.bw.flush(); // bw를 비워서 버퍼에 있는 데이터를 출력
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(protocol.equals(Protocol.ALLERASER)) {  /* protocol 값과 Protocol.ALLERASER 값이 동일한지 비교하는 조건
				    									        equals()메소드는 두 문자열이 동일한지 비교하여 참/거짓 반환*/
				allEraser();
			}		
	} // router 닫힘

		// 모두 지우는 메소드
		private void allEraser() { 
			try {
				for(SocketThread socketThread : vc) { // vc 각 SocktThread에 대해 반복문 실행
					if(socketThread != this) { /* 스레드(this)와 같지 않은 소켓 스레드에 대해서만 조건을 만족
												  재 스레드를 제외한 나머지 소켓 스레드에게만 메시지를 전송하도록 하는 역할을 수행 */
						socketThread.bw.write(Protocol.ALLERASER + ":" + "false" + "\n");  /* 해당 소켓 스레드의 bw 속성을 사용하여 문자열을 기록
						   																	  기록된 문자열은 아직 전송되지 않고 버퍼에 저장 */
					}	
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public String getUserListParse() {
			String sendUsernames = ""; // 문자열 초기화, 문자열은 userList의 요소들을 덧붙여 파싱된 결과를 저장할 변수
			
			for(int i = 0; i < userList.size(); i++ ) {
				//파싱할 ,와 함께 sendusernames에 더해짐
				sendUsernames += userList.get(i) + ",";
			}
			return sendUsernames; //모든 요소에 대해 실행된 후 파싱된 문자열 sendUsernames 반환
		}
		
		public void chattingMsg(String chatMsg) { // 채팅메세지를 전송하는 역할을 수행
			try {
				for(SocketThread socketThread : vc) { 
					if(socketThread != this) { //스레드(this)와 같지 않은 소켓 스레드에 대해서만 조건을 만족, 스레드를 제외한 나머지 소켓 스레드에게만 메시지를 전송하도록 하는 역할을 수행
						socketThread.bw.write(Protocol.CHAT + ":" + chatMsg + "\n");
						socketThread.bw.flush(); // 해당 소켓 스레드의 bw 속성에 저장된 버퍼의 내용을 강제로 전송
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} // chattingMsg()닫힘
		
		// 제시어를 턴의 주인에게 뿌리기
		public void startGame() { //
			System.out.println(TAG + "표시 1 : 성공");
			turnWord = new Word().getStr(); //Word 클래스의 객체 생성 후 getStr 메서드 호출해서 turnWord 변수에 할당
			try {
				if(1 < vc.size()) { // vc 컬렉션(리스트,배열) 크기가 1보다 큰지 확인
					// new CountDown();
					if(currentTurn <= allTurn) {
						for(int i = 0; i < vc.size(); i++) { // vc 컬렉션 크기만큼 반복문 실행
							
							if(i == turn) { 
								//StartGame
								System.out.println(TAG + "표시 2 : 성공");
								System.out.println(TAG + "표시 2 : 메시지 프로토콜 : " + Protocol.STARTGAME + ":" + turnWord);
								vc.get(i).bw.write(Protocol.STARTGAME + ":" + turnWord + "\n"); /* vc 컬렉션에서 i번째 요소를 가져온 후 해당 요소의 bw 속성을 사용하여 문자열을 기록
																								   기록된 문자열은 아직 전송되지 않고 버퍼에 저장 */
								vc.get(i).bw.flush(); /* 버퍼에 저장된 내용을 실제로 전송
														 flush 메서드를 호출함으로써 버퍼에 저장된 내용이 소켓 등을 통해 전송*/
							} 
						}
					}else {
						for(int i = 0; i < allTurn; i++) {//allTurn 변수의 값만큼 반복문 실행
							vc.get(i).bw.write(Protocol.ENDGAME + ":" + "false" + "\n"); /* vc 컬렉션에서 i번째 요소를 가져온 후 해당 요소의 bw 속성을 사용하여 문자열을 기록
																							기록된 문자열은 아직 전송되지 않고 버퍼에 저장 */
							vc.get(i).bw.flush(); // 버퍼에 저장된 내용을 실제로 전송, 버퍼에 저장된 내용이 소켓 등을 통해 전송
						}
					}
					currentTurn++;
					turn++;
					if(turn == vc.size()) { /* vc크기와 같을 경우 (모든 플레이어의 차례가 긑난 경우) 0으로 초기화하여 
					 						   다시 첫 번째 플레이어의 차례로 돌아가게 된다
					 						   모든 플레이어가 한 번씩 차례를 돌았을때 게임을 다시 시작할 수 있도록 하는 조건*/
						turn = 0;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} // startGame() 닫힘
		
		//현재 게임 상태에서 다음 턴을 진행하기 위해 호출하는 메소드
		public void nextTurn() {
			startGame();
			
		}
	} // Thread 닫힘
	
	/* Main 클래스의 main메소드, 프로그램이 실행 될 때 가장 먼저 호출되는 메소드 
	 * Mainserver 클래스의 객체가 생성되고 클래스 내의 생성자가 호출*/
	public static void main(String[] args) { 
		try {
			new Mainserver();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		

}