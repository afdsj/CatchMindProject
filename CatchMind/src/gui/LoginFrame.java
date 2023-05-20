package gui;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.GridLayout;

import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.Border;

import client.MainClient;
import dao.UserDao;

/* JFrame 자바에서 '창'을 만들기 위하여 사용
 * LoginFrame 이벤트를 받기 위해서 JFrame 인터페이스 구현	*/
public class LoginFrame extends JFrame { 


	/* 문자열 타입의 상수 TAG선언 후 초기화
	 * (인터페이스는 상수 필드만 작성 가능, 반드시 선언과 동시에 초기화 해주기) 
	 * 인터페이스가 인터페이스를 상속 받을 시 entends 키워드 인용해서
	 * 여러 인터페이스를 다중 상속 받을 수 있게 한다*/
	private final static String TAG = "SigninFrame : "; 

	// LoginFrame 클래스의 객체 loginFrame 
	public LoginFrame loginFrame = this;
	
	/* JPanel(판넬,도화지)
	 * 자바에서 그래픽을 표현하기 위해서 JPanel 클래스를 상속받아 
	 * paintComponent(Graphics g)을 오버라이딩하여 그 안에 표현하고자 하는 그래픽 명령어
	 * 
	 * JButton
	 * 사용자로부터 명령을 입력받기 위한 목적, 이미지와 문자열로 구성, 생성자를 이용하여 생성
	 * 
	 * JTextField
	 * 한 줄의 문자열을 입력받는 창을 만들 수 있음
	 * 
	 * JPasswordField
	 * 텍스트필드와 다르게 입력내용이 표시되지 않는다
	 *  
	 * ImageIcon
	 * 이미지를 로딩, 조작을 사용할 수 있는 클래스
	 * 이미지 파일의 경로를 넣어주면 이미지 로딩
	 * 로딩 후에 JLabel에 ImageIcon을 설정해주면 해당 이미지 그려짐*/
	public JPanel pLogin; 
	public JButton btID, btPW, btSign, btLogin;
	public JTextField tfID;
	public JPasswordField tfpw;
	public MainClient mainClient; 
	public ImageIcon icon;
//	public ArrayList<String> userName = new ArrayList<>();

	// LoginFrame 생성자를 통해서 초기화 작업 수행 후 프레임 화면에 표시
	public LoginFrame() {
		back(); // 배경 이미지에 관련된 작업을 수해하는 메소드
		initObject(); // 객체를 초기화 작업 수행하는 메소드
		initData(); // 데이터 초기화 작업 수행하는 메소드
		initDesign(); //UI 디자인 초기화 작업을 수행하는 메소드
		initListener(); //이벤트 리스너 초기화 작업을 수행하는 메소드
		setVisible(true); // 호출 프레임이 보이도록 설정
	}

	private void back() {
		icon = new ImageIcon("src/images/loginFrame.png");
		pLogin = new JPanel() { //
			@Override
			public void paintComponents(Graphics g) { //paintComponents메소드 -> 구성요소를 그려주는 메소드,오버라이딩
				/* paintComponents메소드 안에 있는 drawImage메소드를 이용해서 이미지 그리기
				 * setOpaque 불투명성 설정 */
				g.drawImage(icon.getImage(), 0, 0, null);
				setOpaque(false);
			}
		};

	}
	
	// 객체생성
	private void initObject() {
		
//		mainClient = new MainClient(loginFrame);
//		pLogin = new JPanel();
//		pLogin.setBackground(Color.WHITE);

//		btID = new JButton(new ImageIcon("src/images/tbID.png"));
//		btPW = new JButton(new ImageIcon("src/images/tbPw.png"));
		btSign = new JButton(new ImageIcon("src/images/tbSignin.png")); // JButton 객체, sign 이미지
		btLogin = new JButton(new ImageIcon("src/images/tbLogin.png")); // JButton 객체, Login 이미지

		tfID = new JTextField(); // JTextField 객체, 아이디를 입력하는 텍스트 필드
		tfpw = new JPasswordField(); //JPasswordField 객체, 비밀번호 입력하는 텍스트 필드
	}

	// 데이터 초기화
	private void initData() {

	}

	// 디자인
	private void initDesign() {
		// 1. 기본세팅
		loginFrame.setTitle("Login"); // 타이틀 Login으로 설정
		
		loginFrame.setBounds(100, 100, 382, 489); // 위치와 크기 설정, setBounds메소드를 이용해서 x,y좌표, 폭, 높이 지정
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 윈도우 창 종료시 프로세스까지 종료
		loginFrame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0)); // 레이아웃 설정

		// 2. 패널세팅
		getContentPane().add(pLogin); //LoginFrame창에 pLogin 패널 추가
		pLogin.setLayout(null); // 레이아웃 설정

		// 3. 디자인 (위치,크기,테두리 설정)
		tfID.setColumns(10);
		tfID.setBounds(38, 177, 285, 43);
		Border borderLine1 = BorderFactory.createLineBorder(Color.BLACK, 3);
		tfID.setBorder(borderLine1);
//		btID.setBounds(38, 145, 89, 27);
		tfpw.setBounds(38, 269, 285, 43);
		Border borderLine = BorderFactory.createLineBorder(Color.BLACK, 3);
		tfpw.setBorder(borderLine);
//		btPW.setBounds(38, 236, 100, 27);
		tfpw.setColumns(10);
		btSign.setBounds(49, 350, 124, 41);
		btLogin.setBounds(189, 350, 124, 41);

		// 4. 패널에 컴포넌트 추가 (로그인 화면)
//		pLogin.add(btID);
		pLogin.add(tfID); 
//		pLogin.add(btPW);
		pLogin.add(tfpw);
		pLogin.add(btSign);
		pLogin.add(btLogin);
		
		JSeparator separator = new JSeparator(); //JSeparator 객체 생성 (화면에서 구분선을 표시하는 swing 컴포넌트)
		separator.setBounds(111, 281, 120, -1); 
		pLogin.add(separator);
	}

	// 리스너 등록
	private void initListener() {

		btSign.addActionListener(new ActionListener() { // btSign 버튼이 클릭 되었을때 호출 되는 메소드

			@Override
			public void actionPerformed(ActionEvent e) {
				new SigninFrame(mainClient); // SiginFrame 객체 생성 후 mainClient로 초기화
				loginFrame.setVisible(false); //로그인 창 숨기기
			}
		});
		
		btLogin.addActionListener(new ActionListener() {// btLogin 버튼이 클릭 되었을때 호출 되는 메소드

			@Override
			public void actionPerformed(ActionEvent e) {
				// userdao에 select문으로 select from where username 이랑 pw가 같을경우 성공/ 리턴이 0개가되면 오류메시지
				UserDao userDao = UserDao.getInstance(); // 해당 ID,PW 시도
				int result = userDao.로그인(tfID.getText(), tfpw.getText());
				System.out.println(result);
				if (result == 1) { // 로그인 성공했을 경우 게임룸 화면으로 전환 되는 동작 수행
					new GameRoomFrame(tfID.getText()); // 로그인한 ID로 게임룸 화면 생성하는 코드
					loginFrame.setVisible(false); // 로그인 창 숨기고 보이지 않게 함
					// 로그인 성공시 list 에 담아서 push
					String userName = tfID.getText() + ",";
//					System.out.println(TAG + "userN 확인 : " + userN);
//					userName.add(userN);
//					System.out.println(TAG + "userName 확인 : " + userName);
//					mainClient.userSend(userName);
				} else { //false일 경우 입력 필드 초기화
					JOptionPane.showMessageDialog(null, "로그인에 실패했습니다.");
					tfID.setText("");
					tfpw.setText("");
				}

			}
		});

	}
}