package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/* 추상클래스(abstract) : listenAdapter
 * 클래스에서 인터페이스 상속 implements KeyListener,MouseListener,ActionListener
 * 추상 메소드는 반드시 오버라이딩 해야 되기 때문에 오버라이딩을 사용 함*/
public abstract class listenAdapter implements KeyListener, MouseListener, ActionListener{
	
	@Override
	//마우스 버튼을 클릭할 때 호출
	public void mouseClicked(MouseEvent e) { 
	}

	@Override
	//마우스 버튼을 누를 때 호출
	public void mousePressed(MouseEvent e) {
	}

	@Override
	// 마우스 버튼을 눌렀다가 놓을 때 호출
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	// 마우스 커서가 컴포넌트 안으로 들어올때 호출
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	// 마우스 커서가 컴포넌트 밖으로 나갈때 호출
	public void mouseExited(MouseEvent e) {
	}

	@Override
	// 키가 입력 될때 호출
	public void keyTyped(KeyEvent e) {
	}

	@Override
	// 키가 눌릴때 호출
	public void keyPressed(KeyEvent e) {
	}

	@Override
	// 키가 눌린 후 떼어질 때 호출
	public void keyReleased(KeyEvent e) {
	}

	@Override
	// 액션이 발생 했을 때 호출
	public void actionPerformed(ActionEvent e) {
	}
}