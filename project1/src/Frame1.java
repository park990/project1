import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class Frame1 extends JFrame {
    JPanel south_p, north_p, gp1, gp2, gp3, center_p;
    JButton loginB, signupB;
    JMenuBar bar;

    public Frame1() {
        south_p = new JPanel(new GridLayout(3, 0));
        south_p.add(gp1 = new JPanel());

        gp2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.add(north_p = new JPanel(), BorderLayout.NORTH);


        //배경그림 불러오기
        ImageIcon icon1 = new ImageIcon(getClass().getResource("/images/img.png"));
        Image img1 = icon1.getImage();
        Image resized1 = img1.getScaledInstance(350, 350, Image.SCALE_SMOOTH); // 원하는 크기
        ImageIcon resizedIcon1 = new ImageIcon(resized1);

        JLabel l = new JLabel();
        l.setIcon(resizedIcon1);
        center_p = new JPanel();
        center_p.add(l);
        this.add(center_p, BorderLayout.CENTER);

        gp2.add(loginB = new JButton("로그인"));
        loginB.setFont(new Font("굴림", Font.BOLD, 16));
        loginB.setCursor(new Cursor(Cursor.HAND_CURSOR));// 로그인 버튼에 손 모양
        loginB.setPreferredSize(new Dimension(100, 40)); // 버튼 크기


        gp2.add(new JLabel("            "));

        gp2.add(signupB = new JButton("회원가입"));
        signupB.setFont(new Font("굴림", Font.BOLD, 16));
        signupB.setCursor(new Cursor(Cursor.HAND_CURSOR));// 사인업 버튼에 손 모양
        signupB.setPreferredSize(new Dimension(100, 40)); // 버튼 크기
//        signupB.setBackground(new Color(204, 255, 255));  // 연한 하늘색
//        signupB.setFocusPainted(false);
        south_p.add(gp2);

        south_p.add(gp3 = new JPanel());

        this.add(south_p, BorderLayout.SOUTH);

        // 로그인 버튼 입력하면 로그인 화면 나오도록 해보자
        loginB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
                try {
                    new LoginFrame();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }


            }
        });

        // 회원가입 버튼 입력하면 화면 나오도록 ****************************************************
        signupB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SignUP_new();
            }
        });

        this.setTitle("Korea No1 Study Platform");
        this.setBounds(750, 200, 350, 450);
        this.setVisible(true);


        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close(); // 함수%
            }
        });
    } // 생성자의 마지막

    private void close() {
        this.dispose();
    } // 닫기 함수

    public static void main(String[] args) {
        new Frame1();
    }
}