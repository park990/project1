import pm.vo.AdminVO;
import pm.vo.MemberVO;
import pm.vo.StudentVO;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Reader;
import java.util.Random;

public class SignUP_new extends JFrame {

    //패널
    JPanel center_p, bottom_p, phone_p, birth_p, gender_p, bkCode_p;
    //라벨
    JLabel id_l, pw_l, pwInfo_l, pwConfirm_l, name_l, birth_l, gender_l, phone_l, dash_l, dash_2,
            bkCode_l, bkCodeInfo_l, adminUser_l, email_l, address_l;
    //텍스트필드
    JTextField id_tf, name_tf, birth_tf, phone1_tf, phone2_tf, phone3_tf, bkCode_tf, email_tf, address_tf;
    //패스워드필드
    JPasswordField pw_f, pwConfirm_tf;
    //콤보박스
    JComboBox<String> year_cmb, month_cmb, day_cmb;
    //라디오버튼
    JRadioButton male_rbt, female_rbt;
    //버튼
    JButton join_bt, cancel_bt;
    //버튼 그룹
    ButtonGroup gender_group;
    //체크 박스
    JCheckBox adminUser_box;

    SqlSessionFactory factory;

    public SignUP_new() {

        // 프레임 설정
        this.setTitle("회원가입");


        // 중앙 패널 (8행 3열)
        center_p = new JPanel(new GridLayout(13, 3, 10, 10));


        center_p = new JPanel(new GridLayout(12, 3, 10, 10));

        // 바텀 패널
        center_p.setBorder(BorderFactory.createEmptyBorder(
                30, 20, 30, 20)); //안쪽 여백 지정

        // 1. 아이디
        center_p.add(id_l = new JLabel("아이디"));
        center_p.add(id_tf = new JTextField());
        center_p.add(new JLabel()); //1행,3열 공백

        // 2. 비밀번호
        center_p.add(pw_l = new JLabel("비밀번호"));
        center_p.add(pw_f = new JPasswordField());
        center_p.add(pwInfo_l = new JLabel("※ 영문+숫자 포함 8자 이상"));
        pwInfo_l.setFont(new Font("맑은 고딕", Font.ITALIC, 10));
        pwInfo_l.setForeground(Color.GRAY);

        // 3. 비밀번호 확인
        center_p.add(pwConfirm_l = new JLabel("비밀번호 확인"));
        center_p.add(pwConfirm_tf = new JPasswordField());
        center_p.add(new JLabel()); //3행,3열 공백

        // 4. 이름
        center_p.add(name_l = new JLabel("이름"));
        center_p.add(name_tf = new JTextField());
        center_p.add(new JLabel()); //4행,3열 공백

        // 5. 생년월일
        birth_l = new JLabel("생년월일");
        birth_tf = new JTextField();
        year_cmb = new JComboBox<>(); //[년도] 콤보박스
        month_cmb = new JComboBox<>(); //[월] 콤보박스
        day_cmb = new JComboBox<>(); //[일] 콤보박스

        for (int i = 1965; i <= 2025; i++) year_cmb.addItem(i + "년"); //1965~2025년생 선택 가능

        for (int m = 1; m <= 12; m++) {
            month_cmb.addItem(m + "월");
        }
        for (int d = 1; d <= 31; d++) day_cmb.addItem(d + "일"); //1~31일

        month_cmb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int m = month_cmb.getSelectedIndex() + 1;

                day_cmb.removeAllItems();//클릭할때마다 데이 리셋

                if (m == 1|| m == 3|| m == 5|| m == 7|| m == 10|| m == 12 || m == 8) {
                    for (int d = 1; d <= 31; d++) {
                        day_cmb.addItem(d + "일");
                    }
                } else if (m == 2) {
                    for (int d = 1; d <= 28; d++) {
                        day_cmb.addItem(d + "일");
                    }
                } else {
                    for (int d = 1; d <= 30; d++) {
                        day_cmb.addItem(d + "일");
                    }


                }
            }
        });

        birth_p = new JPanel(new GridBagLayout()); //패널을 그리드백 레이아웃 설정
        GridBagConstraints gbc = new GridBagConstraints(); //각 컴포넌트 위치 및 여백을 설정하는 객체
        gbc.insets = new Insets(0, 2, 0, 2); //컴포넌트 주변 여백 설정
        gbc.gridx = 0; //0열에 컴포넌트 배치
        birth_p.add(year_cmb, gbc); //[년도]콤보박스 -> 0번째 열에 추가
        gbc.gridx = 1; //1열에 컴포넌트 배치
        birth_p.add(month_cmb, gbc); //[년도]콤보박스 -> 1번째 열에 추가
        gbc.gridx = 2; //2열에 컴포넌트 배치
        birth_p.add(day_cmb, gbc); //[년도]콤보박스 -> 2번째 열에 추가

        center_p.add(birth_l);
        center_p.add(birth_p);
        center_p.add(new JLabel()); //5행,3열 공백

        // 6. 성별
        gender_l = new JLabel("성별");
        gender_group = new ButtonGroup(); //버튼 그룹 생성
        gender_group.add(male_rbt = new JRadioButton("남성")); //[남성] 라디오버튼을 버튼그룹에 추가
        male_rbt.setSelected(true); //기본값: [남성] 선택
        gender_group.add(female_rbt = new JRadioButton("여성")); //[여성] 라디오버튼을 버튼그룹에 추가

        gender_p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        center_p.add(gender_l);
        gender_p.add(male_rbt);
        gender_p.add(female_rbt);
        center_p.add(gender_p);
        center_p.add(new JLabel()); //6행,3열 공백

        // 7. 전화번호
        phone_p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints(); //컴포넌트 위치,여백 설정하는 객체
        gbc2.insets = new Insets(0, 2, 0, 2); //컴포넌트 주변 여백 설정

        phone_l = new JLabel("전화번호");

        phone1_tf = new JTextField(3);
        phone1_tf.setDocument(new textFieldLimit(4));

        phone2_tf = new JTextField(3);
        phone2_tf.setDocument(new textFieldLimit(4));

        phone3_tf = new JTextField(3);
        phone3_tf.setDocument(new textFieldLimit(4));
        dash_l = new JLabel("-");
        dash_2 = new JLabel("-");

        gbc.gridx = 0;
        phone_p.add(phone1_tf, gbc);
        gbc.gridx = 1;
        phone_p.add(dash_l, gbc);
        gbc.gridx = 2;
        phone_p.add(phone2_tf, gbc);
        gbc.gridx = 3;
        phone_p.add(dash_2, gbc);
        gbc.gridx = 4;
        phone_p.add(phone3_tf, gbc);

        center_p.add(phone_l);
        center_p.add(phone_p);
        center_p.add(new JLabel());

        // 8. 백업코드
        bkCode_l = new JLabel("백업코드");
        bkCode_tf = new JTextField();
        bkCode_tf.setEditable(false);
        bkCodeInfo_l = new JLabel("※ 아이디/패스워드를 찾을때 사용됩니다.");
        bkCodeInfo_l.setFont(new Font("맑은 고딕", Font.ITALIC, 10));
        bkCodeInfo_l.setForeground(Color.GRAY); //폰트색상 회색
        String bkCode = randomCode(); //백업코드 발생함수 결과를 변수에 저장
        bkCode_tf.setText(bkCode); //입력창에 백업코드가 랜덤으로 발생함.
        center_p.add(bkCode_l);
        center_p.add(bkCode_tf);
        center_p.add(bkCodeInfo_l);

        // 9. 이메일
        email_l = new JLabel("이메일");
        email_tf = new JTextField();
        center_p.add(email_l);
        center_p.add(email_tf);
        center_p.add(new JLabel("")); //3열 공백설정

        // 10. 주소
        address_l = new JLabel("주소");
        address_tf = new JTextField();
        JLabel address_l2 = new JLabel("");
        center_p.add(address_l);
        center_p.add(address_tf);
        center_p.add(address_l2);

        // 11. 강사용 체크 박스
        adminUser_l = new JLabel(" ");
        adminUser_box = new JCheckBox("강사용 계정");
        center_p.add(adminUser_l);
        center_p.add(adminUser_box);

        // 버튼 영역
        bottom_p = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        join_bt = new JButton("가입하기");
        join_bt.setCursor(new Cursor(Cursor.HAND_CURSOR));// 가입하기 버튼에 손 모양
        cancel_bt = new JButton("취소하기");
        cancel_bt.setCursor(new Cursor(Cursor.HAND_CURSOR));// 취소하기 버튼에 손 모양

        bottom_p.add(join_bt);
        bottom_p.add(cancel_bt);

        // 패널 붙이기
        this.add(center_p, BorderLayout.CENTER);
        this.add(bottom_p, BorderLayout.SOUTH);

        this.setBounds(750, 250, 650, 550);
        this.setVisible(true);
        //프로그램 종료
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });

        //이벤트 감지자
        //가입버튼 눌렀을때 수행
        join_bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //아이디 유효성 검사
//                String input = id_tf.getText().trim();

                //1. 입력값 수집
                String mem_id = id_tf.getText().trim();
                String mem_pw = new String(pw_f.getPassword()).trim();
                String mem_pw2 = new String(pwConfirm_tf.getPassword()).trim();
                String mem_name = name_tf.getText().trim();
                String phone1 = phone1_tf.getText().trim();
                String phone2 = phone2_tf.getText().trim();
                String phone3 = phone3_tf.getText().trim();
                String mem_phone = phone1 + phone2 + phone3;

                //생년월일 콤보박스 값 가져오기
                String year = (String) year_cmb.getSelectedItem();
                String month = (String) month_cmb.getSelectedItem();
                String day = (String) day_cmb.getSelectedItem();

                String mem_birth = null;
                if (year != null && month != null && day != null) {
                    mem_birth = year.replace("년", "") + "-"
                            + String.format("%02d", Integer.parseInt(month.replace("월", ""))) + "-"
                            + String.format("%02d", Integer.parseInt(day.replace("일", "")));
                }

                //성별
                String mem_gender = male_rbt.isSelected() ? "M" : "F";
                //역할
                String mem_role = adminUser_box.isSelected() ? "A" : "S";
                //이메일, 주소
                String mem_email = email_tf.getText().trim();
                String mem_address = phone1_tf.getText().trim();
                String mem_bkCode = bkCode_tf.getText().trim();

                // 2. 유효성 검사
                if (mem_id.isEmpty() || mem_pw.isEmpty() || mem_pw2.isEmpty() || mem_name.isEmpty()
                        || phone1.isEmpty() || phone2.isEmpty() || phone3.isEmpty()
                        || year == null || month == null || day == null) {
                    JOptionPane.showMessageDialog(SignUP_new.this,
                            "모든 필수 항목을 입력해주세요.");
                    return;
                }

                if (!mem_pw.equals(mem_pw2)) {
                    JOptionPane.showMessageDialog(SignUP_new.this,
                            "비밀번호가 일치하지 않습니다.");
                    return;
                }

                if(!mem_id.matches("^[a-zA-Z0-9]+$")) {
                    JOptionPane.showMessageDialog(SignUP_new.this,
                            "아이디는 영문자와 숫자만 입력 가능합니다.");
                    return;
                }

                //유효성 검사
//                if(id_tf.getText().length() < 1 || pw_f.getText().length() < 1 ||
//                        name_tf.getText().length() < 1 || phone1_tf.getText().length() < 1 ||
//                        phone2_tf.getText().length() < 1 || phone3_tf.getText().length() < 1) {
//                } else {
//                    if (input.equals("")) { //공백일경우
//                        JOptionPane.showMessageDialog(null,
//                                "사용할 아이디를 입력해주세요");
//                        return;
//                    } else if (!input.matches("^[a-zA-Z0-9]+$")) { //영어와 숫자가 아닐때
//                        JOptionPane.showMessageDialog(null,
//                                "아이디는 영문자와 숫자만을 포함합니다.\r\n다시 입력해주세요.");
//                        return;
//                    }

                    try {
                        //3. Mybatis 설정
                        factory_open();
                        SqlSession ss = factory.openSession();
                        //아이디 중복 체크
                        int idCheck = ss.selectOne("member.checkId", mem_id);
                        if (idCheck > 0) {
                            id_tf.setForeground(Color.RED);
                            id_tf.setFont(new Font("맑은 고딕", Font.BOLD, 13));
                            id_tf.setForeground(Color.BLACK);
                            JOptionPane.showMessageDialog(SignUP_new.this,
                                    "이미 존재하는 아이디입니다. 다른 아이디를 입력해주세요.");
                            id_tf.setText(""); //텍스트 초기화
                            return;
                        }

//                        //사용자 입력 값 가져오기
//                        mem_id = new String(id_tf.getText());
//                        mem_pw = new String(pw_f.getPassword());
//                        System.out.println("패스워드: " + mem_pw); //패스워드 확인
//                        mem_name = new String(name_tf.getText());
//                        year = year_cmb.getSelectedItem().toString().replace("년", "");
//                        month = month_cmb.getSelectedItem().toString().replace("월", "");
//                        day = day_cmb.getSelectedItem().toString().replace("일", "");
//                        mem_birth = year + "-" + String.format("%02d", Integer.parseInt(month))
//                                + "-" + String.format("%02d", Integer.parseInt(day));
//                        mem_phone = new String(phone1_tf.getText() + phone2_tf.getText()
//                                + phone3_tf.getText());
//                        mem_gender = male_rbt.isSelected() ? "M" : "F"; //"M" : 남성, "F" : 여성
//                        mem_role = adminUser_box.isSelected() ? "A" : "S"; //강사용, 학생
//                        mem_email = email_tf.getText().trim(); //멤버 email 가져오기
//                        mem_address = address_tf.getText().trim(); //멤버 address 가져오기

                        //vo객체 생성 및 값 설정
                        MemberVO mvo = new MemberVO();
                        mvo.setMem_id(mem_id); //아이디
                        mvo.setMem_pw(mem_pw); //패스워드
                        mvo.setMem_name(mem_name); //이름
                        mvo.setMem_quit("N"); //탈퇴여부, "N" : 탈퇴x
                        mvo.setMem_gender(mem_gender); //성별, "M" : 남성, Default : "M"
                        mvo.setMem_birth(mem_birth); //생년월일
                        mvo.setMem_phone(mem_phone); //전화번호
                        mvo.setMem_bkCode(mem_bkCode); //백업코드
                        mvo.setMem_role(mem_role); //사용자 유형, 강사인지? 학생인지?
                        mvo.setMem_email(mem_email);//멤버 email 추가
                        mvo.setMem_address(mem_address);//멤버 address 추가

//                        if (adminUser_box.isSelected()) {
//                            //mem_t 테이블에서 정보를 가져와서
//                            // std_t 테이블에 값 설정
//                            // admin_t 테이블에 값 설정
                            if (mem_role.equals("S")) { //회원이 학생일경우 수행한다.
                                StudentVO svo = new StudentVO();
                                svo.setStd_name(mem_name);
                                svo.setStd_phone(mem_phone);
                                svo.setStd_address(mem_address);
                                svo.setStd_email(mem_email);
                                ss.insert("student.insertStd", svo);
//                                String str = svo.getStdno();
//                                mvo.setStdno(str);
                                mvo.setStdno(svo.getStdno());
                            } else { //강사일경우 수행한다.
                                AdminVO avo = new AdminVO();
                                avo.setAd_name(mem_name);
                                avo.setAd_phone(mem_phone);
                                avo.setAd_address(mem_address);
                                avo.setAd_email(mem_email);
                                ss.insert("admin.insertAdmin", avo);
//                                String str2 = avo.getAdno();
//                                mvo.setAdno(str2);
                                mvo.setAdno(avo.getAdno());
                            }

                        //6. 최종 insert
                        int res = ss.insert("member.insertMember", mvo);
                        if (res > 0) {
                            JOptionPane.showMessageDialog(SignUP_new.this,
                                    "회원가입 성공!");
                            ss.commit();
                            close(); //창 종료
                        } else {
                            JOptionPane.showMessageDialog(SignUP_new.this,
                                    "회원가입 실패!");
                            ss.rollback();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(SignUP_new.this,
                                "에러 발생: " + ex.getMessage());
                    }
                }
        });
        //취소버튼 눌렀을때 수행
        cancel_bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
    }

    private void factory_open() throws IOException {
        Reader r = Resources.getResourceAsReader("pm/config/conf.xml");
        factory = new SqlSessionFactoryBuilder().build(r);
        r.close();
    }

    //종료
    private void close() {
        dispose();
    }

    //문자열 코드를 랜덤으로 발생시키기
    private String randomCode() {
        Random random = new Random();
        int length = random.nextInt(5) + 5;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int choice = random.nextInt(3);
            switch (choice) {
                case 0:
                    sb.append((char) ((int) random.nextInt(25) + 97));
                    break;
                case 1:
                    sb.append((char) ((int) random.nextInt(25) + 65));
                case 2:
                    sb.append((char) ((int) random.nextInt(10) + 48));
                    break;
                default:
                    break;
            }
        }
//        System.out.println("randomCode = (" + sb + "), length = " + length);
        return sb.toString();
    }

    public static void main(String[] args) {
        new SignUP_new();
    }
}

