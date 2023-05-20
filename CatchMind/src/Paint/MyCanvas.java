package Paint;

import java.awt.Canvas;

import java.awt.Color;
import java.awt.Graphics;

/* Canvas 부모 MyCanvas 자식
 * MyCanvas 클래스는 그래픽을 그리는데 사용되는 화면 영역을 나타내는데 사용*/
public class MyCanvas extends Canvas { 

	// 검은색 점 안찍히게 하기 위해서 x, y 값을 -로 지정해준다.
	public int x;	// int 타입의 필드 : x
	public int y;	// int 타입의 필드 : y
//	public int W = 7;
//	public int H = 7;
	
	/* Color(자료형) color(변수명) = Color.BLACK 
	 * 변수가 생성 될 때 해당 색상으로 초기화
	 * */
	public Color color = Color.BLACK;

	/* paint 메소드 오버라이딩(재정의)
	 * paint 메소드는 Graphics 객체를 매개변수로 받아서 실행
	 * paint'''를 출력한 후에 graphics 객체의 색상을 color로 설정하고
	 * fillOval 메소드를 호출하여 'x-5', ''y-5' 좌표를 중심으로 7*7 크기의 타원을 그린다
	 * */
	@Override
	public void paint(Graphics graphics) {
		System.out.println("paint`````");
		graphics.setColor(color);
		graphics.fillOval(x -5, y -5, 7, 7); // 70, 70 크기의 원 그리기 
	}

	/* update 메소드 오버라이딩(재정의)
	 * update 메소드는 Graphics 객체를 매개변수로 받아서 실행
	 * update'''를 출력한 후에 paint 메소드 호출
	 * paint 메소드에서는 그래픽을 그리는 작업을 수행
	 * */
	@Override
	public void update(Graphics graphics) {
		System.out.println("update`````");
		paint(graphics);
	}
	
	/* setter(설정자) 메소드
	 * 필드 x, 매개변수 int x 
	 * this.참조변수 : 메소드에 접근하고 있는 자신을 참조한다*/
	public void setX(int x) {
		this.x = x;
	}
	/* 필드 y, 매개변수 int y 
	 * this.참조변수 : 메소드에 접근하고 있는 자신을 참조한다*/
	public void setY(int y) {
		this.y = y;
	}

}

