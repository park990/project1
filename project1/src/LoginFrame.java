import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import pm.vo.MemberVO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;


public class LoginFrame extends JFrame {
    JPanel north_p, Np1;
    JPanel center_p, cenN, Cgp1, Cgp1_1, CgpNull;
    JLabel lpageL, ilable;
    JTextField input_ID;
    JPasswordField input_PW;
    JButton b1;
    JCheckBox teacher_ckbox;
    SqlSessionFactory factory;
    findFrame ff;
    SqlSession ss;
    String idstring, pwstring;


    public LoginFrame() throws IOException {
        Cursor hand = new Cursor(Cursor.HAND_CURSOR);
        this.setTitle("로그인  ");

        init();
        north_p = new JPanel(new BorderLayout());

        ImageIcon icon1 = new ImageIcon(getClass().getResource("/images/img.png"));
        Image img1 = icon1.getImage();
        Image resized1 = img1.getScaledInstance(180, 180, Image.SCALE_SMOOTH); // 원하는 크기
        ImageIcon resizedIcon1 = new ImageIcon(resized1);

        ilable = new JLabel();
        ilable.setIcon(resizedIcon1);
        Np1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, -26));
        Np1.add(ilable);

        north_p.add(Np1);
        north_p.setBorder(BorderFactory.createEmptyBorder(10, 0, -10, 0));
        this.add(north_p, BorderLayout.NORTH);

        // Login Page라고 나오는 레이블 설정
        lpageL = new JLabel("Login Page");
        lpageL.setFont(new Font("돋음", Font.BOLD, 18));
        lpageL.setForeground(new Color(0, 151, 210));

        // 센터 Nort에 들어갈 login page cenN에 삽입
        cenN = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        cenN.add(lpageL);

        center_p = new JPanel(new BorderLayout());
        center_p.add(cenN, BorderLayout.NORTH);

        // 패널
        Cgp1 = new JPanel(new GridLayout(3, 2, 5, 0));
        Cgp1.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 0));

        // 아이디 레이블 삽입
        JLabel idLabel = new JLabel("ID:");
        idLabel.setHorizontalAlignment(SwingConstants.RIGHT); //그리드 레이아웃 오른쪽정렬
        Cgp1.add(idLabel);

        // 아이디 텍스트 필드 조정 후 삽입
        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));//텍스트 right랑 간격
        input_ID = new JTextField(10);
        input_ID.setPreferredSize(new Dimension(100, 25));
        idPanel.add(input_ID);
        Cgp1.add(idPanel);  // 원래 input_ID 자리에 넣기

        // PW 레이블 삽입
        JLabel pwLabel = new JLabel("PW:");
        pwLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        input_PW = new JPasswordField(5);
        Cgp1.add(pwLabel);

        // PW 텍스트 필드 조정 후 삽입
        JPanel pwPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));//텍스트 right랑 간격
        input_PW = new JPasswordField(10);
        input_PW.setPreferredSize(new Dimension(100, 25));
        pwPanel.add(input_PW);
        Cgp1.add(pwPanel);

        // 공백 패널 입력   >> 그래야 그리드 화면 마지막칸에 로그인 버튼이랑 강사체크박스 삽입가능
        CgpNull = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        Cgp1.add(CgpNull);

        // 로그인버튼
        b1 = new JButton("로그인");
        b1.setCursor(hand);

        Cgp1_1 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 1, 0));// 강사와 로그인 사이의 간격을 띄움

        // 강사 체크박스와 버튼 패널에 삽입
        Cgp1_1.add(teacher_ckbox = new JCheckBox("강사"));
        teacher_ckbox.setCursor(hand);
        Cgp1_1.add(b1);

        // 강사 로그인들어간 패널과 전체화면과의 간격 설정
        Cgp1_1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 9));

        //체크박스와 로그인 버튼이 들어간 패널을 삽입
        Cgp1.add(Cgp1_1);

        center_p.add(Cgp1);


        this.add(center_p);


        this.setBounds(750 + (350 / 2 / 2), 200 + (500 / 2 / 2), 290, 310);
        this.setResizable(false);
        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        b1.addActionListener(new ActionListener() {//로그인 권한 인증 구현*******************************
            @Override
            public void actionPerformed(ActionEvent e) {
                idstring = input_ID.getText();
                pwstring = new String(input_PW.getPassword());
                ss = factory.openSession();// 펙토리 sql

                if (teacher_ckbox.isSelected()) {// 강사용 로그인 ****************************************************************

                    System.out.println("강사 아이디: " + idstring + "\r\n" + "강사 비밀번호: " + pwstring);
                    boolean b =mem_if_vo_isnull("member.adm_login");
                    if(b){//강사로그인에 성공햇다면
                        new AdminFrame();
                        dispose();
                    }

                } else {// 학생용 로그인*********************************************************************************

                    System.out.println("입력한 아이디: " + idstring + "\r\n" + "입력한 비밀번호: " + pwstring);

                    boolean b =mem_if_vo_isnull("member.st_login");// %%%함수

                    if(b){//학생 로그인에 성공했다면


                    }
                }
                //인증 과정 끝


            }
        });// 로그인 버튼 클릭 관리자


    }// 생성자의 마지막

    private boolean mem_if_vo_isnull(String target) { //학생용 로그인 인증
        boolean s=false;
        Map<String, String> m = new HashMap<>();
        m.put("ID", idstring);
        m.put("PW", pwstring);   //map 으로 하고 픈데

        MemberVO vo = ss.selectOne(target, m);

        if (vo == null) {//로그인 실패

            showfindFrame(); // 함수&&

        } else {
            JOptionPane.showMessageDialog(LoginFrame.this, "로그인 완료!");
            s=true;
        }
        ss.close();
        return s;
    }// 로그인 인증함수



    private void showfindFrame() {
        Object[] options = {"Find ID & PW", "Close"};

        int n = JOptionPane.showOptionDialog(LoginFrame.this,
                "입력된 정보를 다시 확인해주세요.",
                "로그인 오류",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[1]);

        if (n == 0) { // 찾기 클릭하면
            // 아이디 찾기 로직
            System.out.println("찾기");


            try {
                ff = new findFrame();
                ff.setVisible(true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        input_ID.setText("");
        input_PW.setText("");
    }


    private void init() throws IOException {
        Reader r = Resources.getResourceAsReader("pm/config/conf.xml");
        factory = new SqlSessionFactoryBuilder().build(r);
        r.close();
    }

    ;

    public static void main(String[] args) throws IOException {
        new LoginFrame();
    }
}
