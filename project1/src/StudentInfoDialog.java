import org.apache.ibatis.session.SqlSession;
import pm.vo.MemberVO;
import pm.vo.StudentVO;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Reader;
import java.util.List;

//학생 상세 창
public class StudentInfoDialog extends JDialog {

    SqlSessionFactory factory;
    CardLayout cardLayout;

    public StudentInfoDialog(AdminFrame parent, boolean modal, StudentVO stVO) {
        super(parent, modal);
        initComponents();

        //cardLayout 초기화
        cardLayout = new CardLayout();
        jPanel1.setLayout(cardLayout);

        jPanel1.removeAll();

        jPanel1.add(jPanel9, "stdInfo");     // 수강생 정보
        jPanel1.add(jPanel13, "stdModify");     //수강생 수정

        this.setBounds(300,200,380,580);

        this.setTitle("학생 상세정보");

        init(); //db연결

        //연동되는지 콘솔에 출력
        //System.out.println(stVO.getStd_name());

        //학생정보 db연동하여 출력
        jTextField1.setText(stVO.getStd_name());
        jTextField2.setText(stVO.getStdno());


        // result map 활용 재윤*********************************
        SqlSession ss = factory.openSession();
        StudentVO svoMVO = ss.selectOne("student.get_memID",stVO.getStdno());
        jTextField3.setText(svoMVO.getMvo().getMem_id()); //회원 ID **********************************
        // result map 활용 재윤**********************************

        jTextField4.setText(stVO.getStd_phone());
        jTextField6.setText(stVO.getStd_address());
        jTextField7.setText(stVO.getStd_email());

        jTextField5.setText(stVO.getStd_name());
        jTextField8.setText(stVO.getStdno());
        //jTextField9.setText(stVO.getStd_phone()); //회원 ID
        jTextField10.setText(stVO.getStd_phone());
        jTextField11.setText(stVO.getStd_address());
        jTextField12.setText(stVO.getStd_email());

        //수정 버튼 클릭 시
        StdModify_bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //수정 화면으로 전환 //(부모값 , "지정이름")
                cardLayout.show(jPanel1, "stdModify");

            }
        });

        //취소 버튼 클릭 시
        StdCancle_bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                dispose(); //현재 창객체를 메모리상에서 삭제
            }
        });

        //저장 버튼 클릭 시
        stdSave_bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("수강생 데이터 저장!!!");

                String std_name = jTextField5.getText().trim();
                String stdno = jTextField8.getText().trim();
                //String std_name = jTextField5.getText().trim();  //회원 아이디

                String std_phone = jTextField10.getText().trim();
                String std_address = jTextField11.getText().trim();
                String std_email = jTextField12.getText().trim();

                StudentVO stdVO = new StudentVO();
                stdVO.setStd_name(std_name);
                stdVO.setStdno(stdno);
                //stdVO.setStd_name(std_name); //회원 아이디
                stdVO.setStd_phone(std_phone);
                stdVO.setStd_address(std_address);
                stdVO.setStd_email(std_email);

                try{
                    parent.stdUpdateData(stdVO);
                    JOptionPane.showMessageDialog(StudentInfoDialog.this, "수정이 완료되었습니다.");
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(StudentInfoDialog.this, "수정 중 오류가 발생하였습니다.:\n" + ex.getMessage());
                    ex.printStackTrace(); // 콘솔에 에러 로그 출력 (디버깅용)
                }
                
                dispose(); //현재 창객체를 메모리상에서 삭제

                //화면 전환 //(부모값 , "지정이름")
                cardLayout.show(jPanel1, "stdModify");
            }
        });

        //수정페이지에서 취소 버튼 클릭 시
        stdSaveCancle_bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dispose(); //현재 창객체를 메모리상에서 삭제
            }
        });

        this.setVisible(true); //창 띄우기
    }

    //DB연결
    private void init(){
        try{
            Reader r = Resources.getResourceAsReader("pm/config/conf.xml");
            factory = new SqlSessionFactoryBuilder().build(r);
            r.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    

    private void initComponents() {

        jPanel2 = new JPanel();
        jPanel3 = new JPanel();
        jLabel1 = new JLabel();
        jPanel4 = new JPanel();
        jLabel2 = new JLabel();
        jLabel11 = new JLabel();
        jPanel5 = new JPanel();
        jLabel3 = new JLabel();
        jLabel12 = new JLabel();
        jPanel6 = new JPanel();
        jLabel4 = new JLabel();
        jLabel13 = new JLabel();
        jPanel7 = new JPanel();
        jLabel5 = new JLabel();
        jLabel14 = new JLabel();
        jPanel8 = new JPanel();
        jButton1 = new JButton();
        jButton2 = new JButton();
        jPanel1 = new JPanel(cardLayout);
        jPanel9 = new JPanel();
        jPanel10 = new JPanel();
        jLabel6 = new JLabel();
        jPanel11 = new JPanel();
        jPanel16 = new JPanel();
        jPanel17 = new JPanel();
        jPanel18 = new JPanel();
        jPanel12 = new JPanel();
        jLabel8 = new JLabel();
        jTextField1 = new JTextField();
        jPanel14 = new JPanel();
        jLabel9 = new JLabel();
        jTextField2 = new JTextField();
        jPanel19 = new JPanel();
        jPanel20 = new JPanel();
        jPanel21 = new JPanel();
        jPanel32 = new JPanel();
        jPanel15 = new JPanel();
        jLabel10 = new JLabel();
        jTextField3 = new JTextField();
        jPanel33 = new JPanel();
        jLabel18 = new JLabel();
        jTextField4 = new JTextField();
        jPanel23 = new JPanel();
        jPanel24 = new JPanel();
        jPanel25 = new JPanel();
        jPanel34 = new JPanel();
        jPanel35 = new JPanel();
        jLabel19 = new JLabel();
        jTextField6 = new JTextField();
        jPanel36 = new JPanel();
        jLabel20 = new JLabel();
        jTextField7 = new JTextField();
        jPanel26 = new JPanel();
        jPanel27 = new JPanel();
        jPanel28 = new JPanel();
        jPanel37 = new JPanel();
        jPanel38 = new JPanel();
        jPanel42 = new JPanel();
        StdModify_bt = new JButton();
        StdCancle_bt = new JButton();
        jPanel13 = new JPanel();
        jPanel22 = new JPanel();
        jLabel7 = new JLabel();
        jPanel29 = new JPanel();
        jPanel30 = new JPanel();
        jPanel31 = new JPanel();
        jPanel39 = new JPanel();
        jPanel40 = new JPanel();
        jLabel15 = new JLabel();
        jTextField5 = new JTextField();
        jPanel41 = new JPanel();
        jLabel16 = new JLabel();
        jTextField8 = new JTextField();
        jPanel43 = new JPanel();
        jPanel44 = new JPanel();
        jPanel45 = new JPanel();
        jPanel46 = new JPanel();
        jPanel47 = new JPanel();
        jLabel17 = new JLabel();
        jTextField9 = new JTextField();
        jPanel48 = new JPanel();
        jLabel21 = new JLabel();
        jTextField10 = new JTextField();
        jPanel49 = new JPanel();
        jPanel50 = new JPanel();
        jPanel51 = new JPanel();
        jPanel52 = new JPanel();
        jPanel53 = new JPanel();
        jLabel22 = new JLabel();
        jTextField11 = new JTextField();
        jPanel54 = new JPanel();
        jLabel23 = new JLabel();
        jTextField12 = new JTextField();
        jPanel55 = new JPanel();
        jPanel56 = new JPanel();
        jPanel57 = new JPanel();
        jPanel58 = new JPanel();
        jPanel59 = new JPanel();
        jPanel60 = new JPanel();
        stdSave_bt = new JButton();
        stdSaveCancle_bt = new JButton();

        jPanel2.setLayout(new GridLayout(6, 1));

        jLabel1.setFont(new Font("맑은 고딕", 0, 18)); // NOI18N
        jLabel1.setText("학생 상세 정보");
        jPanel3.add(jLabel1);

        jPanel2.add(jPanel3);

        jPanel4.setLayout(new FlowLayout(FlowLayout.RIGHT));

        jLabel2.setFont(new Font("맑은 고딕", 0, 14)); // NOI18N
        jLabel2.setText("이름:");
        jPanel4.add(jLabel2);

        jLabel11.setFont(new Font("맑은 고딕", 0, 14)); // NOI18N
        jLabel11.setText("jLabel11");
        jPanel4.add(jLabel11);

        jPanel2.add(jPanel4);

        jPanel5.setLayout(new FlowLayout(FlowLayout.RIGHT));

        jLabel3.setFont(new Font("맑은 고딕", 0, 14)); // NOI18N
        jLabel3.setText("전화번호:");
        jPanel5.add(jLabel3);

        jLabel12.setFont(new Font("맑은 고딕", 0, 14)); // NOI18N
        jLabel12.setText("jLabel12");
        jPanel5.add(jLabel12);

        jPanel2.add(jPanel5);

        jPanel6.setLayout(new FlowLayout(FlowLayout.RIGHT));

        jLabel4.setFont(new Font("맑은 고딕", 0, 14)); // NOI18N
        jLabel4.setText("이메일:");
        jPanel6.add(jLabel4);

        jLabel13.setFont(new Font("맑은 고딕", 0, 14)); // NOI18N
        jLabel13.setText("jLabel13");
        jPanel6.add(jLabel13);

        jPanel2.add(jPanel6);

        jPanel7.setLayout(new FlowLayout(FlowLayout.RIGHT));

        jLabel5.setFont(new Font("맑은 고딕", 0, 14)); // NOI18N
        jLabel5.setText("주소:");
        jPanel7.add(jLabel5);

        jLabel14.setFont(new Font("맑은 고딕", 0, 14)); // NOI18N
        jLabel14.setText("jLabel14");
        jPanel7.add(jLabel14);

        jPanel2.add(jPanel7);

        jPanel8.setLayout(new FlowLayout(FlowLayout.RIGHT));

        jButton1.setText("수정");
        jPanel8.add(jButton1);

        jButton2.setText("닫기");
        jPanel8.add(jButton2);

        jPanel2.add(jPanel8);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        //jPanel1.setLayout(new java.awt.CardLayout());

        jPanel9.setLayout(new GridLayout(5, 1));

        jLabel6.setFont(new Font("맑은 고딕", 0, 18)); // NOI18N
        jLabel6.setText("학생 상세 정보");
        jPanel10.add(jLabel6);

        jPanel9.add(jPanel10);

        jPanel11.setLayout(new BorderLayout());

        jPanel16.setPreferredSize(new Dimension(30, 100));

        GroupLayout jPanel16Layout = new GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
                jPanel16Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
                jPanel16Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 106, Short.MAX_VALUE)
        );

        jPanel11.add(jPanel16, BorderLayout.LINE_START);

        jPanel17.setPreferredSize(new Dimension(30, 100));

        GroupLayout jPanel17Layout = new GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
                jPanel17Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
                jPanel17Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 106, Short.MAX_VALUE)
        );

        jPanel11.add(jPanel17, BorderLayout.LINE_END);

        jPanel18.setLayout(new GridLayout(2, 0));

        jLabel8.setText("이름:");

        jTextField1.setText("");
        jTextField1.setColumns(18);
        jTextField1.setEditable(false);
        jTextField1.setBackground(new Color(242, 242, 242));

        GroupLayout jPanel12Layout = new GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
                jPanel12Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel8)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28))
        );
        jPanel12Layout.setVerticalGroup(
                jPanel12Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                .addContainerGap(19, Short.MAX_VALUE)
                                .addGroup(jPanel12Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel8)
                                        .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(11, 11, 11))
        );

        jPanel18.add(jPanel12);

        jLabel9.setText("학번: ");

        jTextField2.setColumns(18);
        jTextField2.setEditable(false);
        jTextField2.setBackground(new Color(242, 242, 242));

        GroupLayout jPanel14Layout = new GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
                jPanel14Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel14Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel9)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27))
        );
        jPanel14Layout.setVerticalGroup(
                jPanel14Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                                .addContainerGap(19, Short.MAX_VALUE)
                                .addGroup(jPanel14Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel9)
                                        .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(11, 11, 11))
        );

        jPanel18.add(jPanel14);

        jPanel11.add(jPanel18, BorderLayout.CENTER);

        jPanel9.add(jPanel11);

        jPanel19.setLayout(new BorderLayout());

        jPanel20.setPreferredSize(new Dimension(30, 100));

        GroupLayout jPanel20Layout = new GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
                jPanel20Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
                jPanel20Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 106, Short.MAX_VALUE)
        );

        jPanel19.add(jPanel20, BorderLayout.LINE_START);

        jPanel21.setPreferredSize(new Dimension(30, 100));

        GroupLayout jPanel21Layout = new GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
                jPanel21Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
                jPanel21Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 106, Short.MAX_VALUE)
        );

        jPanel19.add(jPanel21, BorderLayout.LINE_END);

        jPanel32.setLayout(new GridLayout(2, 0));

        jLabel10.setText("아이디:");

        jTextField3.setColumns(18);
        jTextField3.setEditable(false);
        jTextField3.setBackground(new Color(242, 242, 242));

        GroupLayout jPanel15Layout = new GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
                jPanel15Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel15Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel10)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26))
        );
        jPanel15Layout.setVerticalGroup(
                jPanel15Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                                .addContainerGap(19, Short.MAX_VALUE)
                                .addGroup(jPanel15Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel10)
                                        .addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(11, 11, 11))
        );

        jPanel32.add(jPanel15);

        jLabel18.setText("연락처:");

        jTextField4.setColumns(18);
        jTextField4.setEditable(false);
        jTextField4.setBackground(new Color(242, 242, 242));

        GroupLayout jPanel33Layout = new GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
                jPanel33Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel33Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel18)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField4, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27))
        );
        jPanel33Layout.setVerticalGroup(
                jPanel33Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                                .addContainerGap(19, Short.MAX_VALUE)
                                .addGroup(jPanel33Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel18)
                                        .addComponent(jTextField4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(11, 11, 11))
        );

        jPanel32.add(jPanel33);

        jPanel19.add(jPanel32, BorderLayout.CENTER);

        jPanel9.add(jPanel19);

        jPanel23.setLayout(new BorderLayout());

        jPanel24.setPreferredSize(new Dimension(30, 100));

        GroupLayout jPanel24Layout = new GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
                jPanel24Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
                jPanel24Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 106, Short.MAX_VALUE)
        );

        jPanel23.add(jPanel24, BorderLayout.LINE_START);

        jPanel25.setPreferredSize(new Dimension(30, 100));

        GroupLayout jPanel25Layout = new GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
                jPanel25Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel25Layout.setVerticalGroup(
                jPanel25Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 106, Short.MAX_VALUE)
        );

        jPanel23.add(jPanel25, BorderLayout.LINE_END);

        jPanel34.setLayout(new GridLayout(2, 0));

        jLabel19.setText("주소:");

        jTextField6.setColumns(18);
        jTextField6.setEditable(false);
        jTextField6.setBackground(new Color(242, 242, 242));

        GroupLayout jPanel35Layout = new GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
                jPanel35Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel35Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel19)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField6, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26))
        );
        jPanel35Layout.setVerticalGroup(
                jPanel35Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel35Layout.createSequentialGroup()
                                .addContainerGap(19, Short.MAX_VALUE)
                                .addGroup(jPanel35Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel19)
                                        .addComponent(jTextField6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(11, 11, 11))
        );

        jPanel34.add(jPanel35);

        jLabel20.setText("e-mail:");

        jTextField7.setColumns(18);
        jTextField7.setEditable(false);
        jTextField7.setBackground(new Color(242, 242, 242));


        GroupLayout jPanel36Layout = new GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
                jPanel36Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel36Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel20)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField7, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25))
        );
        jPanel36Layout.setVerticalGroup(
                jPanel36Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel36Layout.createSequentialGroup()
                                .addContainerGap(19, Short.MAX_VALUE)
                                .addGroup(jPanel36Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel20)
                                        .addComponent(jTextField7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(11, 11, 11))
        );

        jPanel34.add(jPanel36);

        jPanel23.add(jPanel34, BorderLayout.CENTER);

        jPanel9.add(jPanel23);

        jPanel26.setLayout(new BorderLayout());

        jPanel27.setPreferredSize(new Dimension(30, 100));

        GroupLayout jPanel27Layout = new GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
                jPanel27Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel27Layout.setVerticalGroup(
                jPanel27Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 106, Short.MAX_VALUE)
        );

        jPanel26.add(jPanel27, BorderLayout.LINE_START);

        jPanel28.setPreferredSize(new Dimension(30, 100));

        GroupLayout jPanel28Layout = new GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
                jPanel28Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel28Layout.setVerticalGroup(
                jPanel28Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 106, Short.MAX_VALUE)
        );

        jPanel26.add(jPanel28, BorderLayout.LINE_END);

        jPanel37.setLayout(new GridLayout(2, 0));

        GroupLayout jPanel38Layout = new GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
                jPanel38Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 317, Short.MAX_VALUE)
        );
        jPanel38Layout.setVerticalGroup(
                jPanel38Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 53, Short.MAX_VALUE)
        );

        jPanel37.add(jPanel38);

        StdModify_bt.setText("수정");

        StdCancle_bt.setText("취소");

        GroupLayout jPanel42Layout = new GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
                jPanel42Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel42Layout.createSequentialGroup()
                                .addGap(130, 130, 130)
                                .addComponent(StdModify_bt, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(StdCancle_bt, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel42Layout.setVerticalGroup(
                jPanel42Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel42Layout.createSequentialGroup()
                                .addGroup(jPanel42Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(StdModify_bt, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(StdCancle_bt, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 22, Short.MAX_VALUE))
        );

        jPanel37.add(jPanel42);

        jPanel26.add(jPanel37, BorderLayout.CENTER);

        jPanel9.add(jPanel26);

        jPanel13.setLayout(new GridLayout(5, 1));

        jLabel7.setFont(new Font("맑은 고딕", 0, 18)); // NOI18N
        jLabel7.setText("학생 정보 수정");
        jPanel22.add(jLabel7);

        jPanel13.add(jPanel22);

        jPanel29.setLayout(new BorderLayout());

        jPanel30.setPreferredSize(new Dimension(30, 100));

        GroupLayout jPanel30Layout = new GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
                jPanel30Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel30Layout.setVerticalGroup(
                jPanel30Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 106, Short.MAX_VALUE)
        );

        jPanel29.add(jPanel30, BorderLayout.LINE_START);

        jPanel31.setPreferredSize(new Dimension(30, 100));

        GroupLayout jPanel31Layout = new GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
                jPanel31Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel31Layout.setVerticalGroup(
                jPanel31Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 106, Short.MAX_VALUE)
        );

        jPanel29.add(jPanel31, BorderLayout.LINE_END);

        jPanel39.setLayout(new GridLayout(2, 0));

        jLabel15.setText("이름:");

        jTextField5.setColumns(18);
        jTextField5.setEditable(false);

        GroupLayout jPanel40Layout = new GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
                jPanel40Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel40Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel15)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField5, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28))
        );
        jPanel40Layout.setVerticalGroup(
                jPanel40Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel40Layout.createSequentialGroup()
                                .addContainerGap(19, Short.MAX_VALUE)
                                .addGroup(jPanel40Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel15)
                                        .addComponent(jTextField5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(11, 11, 11))
        );

        jPanel39.add(jPanel40);

        jLabel16.setText("학번: ");

        jTextField8.setColumns(18);
        jTextField8.setEditable(false);

        GroupLayout jPanel41Layout = new GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
                jPanel41Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel41Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel16)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField8, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27))
        );
        jPanel41Layout.setVerticalGroup(
                jPanel41Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel41Layout.createSequentialGroup()
                                .addContainerGap(19, Short.MAX_VALUE)
                                .addGroup(jPanel41Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel16)
                                        .addComponent(jTextField8, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(11, 11, 11))
        );

        jPanel39.add(jPanel41);

        jPanel29.add(jPanel39, BorderLayout.CENTER);

        jPanel13.add(jPanel29);

        jPanel43.setLayout(new BorderLayout());

        jPanel44.setPreferredSize(new Dimension(30, 100));

        GroupLayout jPanel44Layout = new GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
                jPanel44Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel44Layout.setVerticalGroup(
                jPanel44Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 106, Short.MAX_VALUE)
        );

        jPanel43.add(jPanel44, BorderLayout.LINE_START);

        jPanel45.setPreferredSize(new Dimension(30, 100));

        GroupLayout jPanel45Layout = new GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
                jPanel45Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel45Layout.setVerticalGroup(
                jPanel45Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 106, Short.MAX_VALUE)
        );

        jPanel43.add(jPanel45, BorderLayout.LINE_END);

        jPanel46.setLayout(new GridLayout(2, 0));

        jLabel17.setText("아이디:");

        jTextField9.setColumns(18);
        jTextField9.setEditable(false);

        GroupLayout jPanel47Layout = new GroupLayout(jPanel47);
        jPanel47.setLayout(jPanel47Layout);
        jPanel47Layout.setHorizontalGroup(
                jPanel47Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel47Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel17)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField9, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26))
        );
        jPanel47Layout.setVerticalGroup(
                jPanel47Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel47Layout.createSequentialGroup()
                                .addContainerGap(19, Short.MAX_VALUE)
                                .addGroup(jPanel47Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel17)
                                        .addComponent(jTextField9, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(11, 11, 11))
        );

        jPanel46.add(jPanel47);

        jLabel21.setText("연락처:");

        jTextField10.setColumns(18);

        GroupLayout jPanel48Layout = new GroupLayout(jPanel48);
        jPanel48.setLayout(jPanel48Layout);
        jPanel48Layout.setHorizontalGroup(
                jPanel48Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel48Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel21)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField10, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27))
        );
        jPanel48Layout.setVerticalGroup(
                jPanel48Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel48Layout.createSequentialGroup()
                                .addContainerGap(19, Short.MAX_VALUE)
                                .addGroup(jPanel48Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel21)
                                        .addComponent(jTextField10, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(11, 11, 11))
        );

        jPanel46.add(jPanel48);

        jPanel43.add(jPanel46, BorderLayout.CENTER);

        jPanel13.add(jPanel43);

        jPanel49.setLayout(new BorderLayout());

        jPanel50.setPreferredSize(new Dimension(30, 100));

        GroupLayout jPanel50Layout = new GroupLayout(jPanel50);
        jPanel50.setLayout(jPanel50Layout);
        jPanel50Layout.setHorizontalGroup(
                jPanel50Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel50Layout.setVerticalGroup(
                jPanel50Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 106, Short.MAX_VALUE)
        );

        jPanel49.add(jPanel50, BorderLayout.LINE_START);

        jPanel51.setPreferredSize(new Dimension(30, 100));

        GroupLayout jPanel51Layout = new GroupLayout(jPanel51);
        jPanel51.setLayout(jPanel51Layout);
        jPanel51Layout.setHorizontalGroup(
                jPanel51Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel51Layout.setVerticalGroup(
                jPanel51Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 106, Short.MAX_VALUE)
        );

        jPanel49.add(jPanel51, BorderLayout.LINE_END);

        jPanel52.setLayout(new GridLayout(2, 0));

        jLabel22.setText("주소:");

        jTextField11.setColumns(18);

        GroupLayout jPanel53Layout = new GroupLayout(jPanel53);
        jPanel53.setLayout(jPanel53Layout);
        jPanel53Layout.setHorizontalGroup(
                jPanel53Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel53Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel22)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField11, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26))
        );
        jPanel53Layout.setVerticalGroup(
                jPanel53Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel53Layout.createSequentialGroup()
                                .addContainerGap(19, Short.MAX_VALUE)
                                .addGroup(jPanel53Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel22)
                                        .addComponent(jTextField11, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(11, 11, 11))
        );

        jPanel52.add(jPanel53);

        jLabel23.setText("e-mail:");

        jTextField12.setColumns(18);

        GroupLayout jPanel54Layout = new GroupLayout(jPanel54);
        jPanel54.setLayout(jPanel54Layout);
        jPanel54Layout.setHorizontalGroup(
                jPanel54Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel54Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel23)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField12, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25))
        );
        jPanel54Layout.setVerticalGroup(
                jPanel54Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel54Layout.createSequentialGroup()
                                .addContainerGap(19, Short.MAX_VALUE)
                                .addGroup(jPanel54Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel23)
                                        .addComponent(jTextField12, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(11, 11, 11))
        );

        jPanel52.add(jPanel54);

        jPanel49.add(jPanel52, BorderLayout.CENTER);

        jPanel13.add(jPanel49);

        jPanel55.setLayout(new BorderLayout());

        jPanel56.setPreferredSize(new Dimension(30, 100));

        GroupLayout jPanel56Layout = new GroupLayout(jPanel56);
        jPanel56.setLayout(jPanel56Layout);
        jPanel56Layout.setHorizontalGroup(
                jPanel56Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel56Layout.setVerticalGroup(
                jPanel56Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 106, Short.MAX_VALUE)
        );

        jPanel55.add(jPanel56, BorderLayout.LINE_START);

        jPanel57.setPreferredSize(new Dimension(30, 100));

        GroupLayout jPanel57Layout = new GroupLayout(jPanel57);
        jPanel57.setLayout(jPanel57Layout);
        jPanel57Layout.setHorizontalGroup(
                jPanel57Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel57Layout.setVerticalGroup(
                jPanel57Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 106, Short.MAX_VALUE)
        );

        jPanel55.add(jPanel57, BorderLayout.LINE_END);

        jPanel58.setLayout(new GridLayout(2, 0));

        GroupLayout jPanel59Layout = new GroupLayout(jPanel59);
        jPanel59.setLayout(jPanel59Layout);
        jPanel59Layout.setHorizontalGroup(
                jPanel59Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 317, Short.MAX_VALUE)
        );
        jPanel59Layout.setVerticalGroup(
                jPanel59Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 53, Short.MAX_VALUE)
        );

        jPanel58.add(jPanel59);

        stdSave_bt.setText("저장");

        stdSaveCancle_bt.setText("취소");

        GroupLayout jPanel60Layout = new GroupLayout(jPanel60);
        jPanel60.setLayout(jPanel60Layout);
        jPanel60Layout.setHorizontalGroup(
                jPanel60Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel60Layout.createSequentialGroup()
                                .addGap(130, 130, 130)
                                .addComponent(stdSave_bt, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(stdSaveCancle_bt, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel60Layout.setVerticalGroup(
                jPanel60Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel60Layout.createSequentialGroup()
                                .addGroup(jPanel60Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(stdSave_bt, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(stdSaveCancle_bt, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 22, Short.MAX_VALUE))
        );

        jPanel58.add(jPanel60);

        jPanel55.add(jPanel58, BorderLayout.CENTER);

        jPanel13.add(jPanel55);


        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, 532, GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>



    // Variables declaration - do not modify
    private JButton jButton1;
    private JButton jButton2;
    private JButton StdModify_bt;
    private JButton StdCancle_bt;
    private JButton stdSave_bt;
    private JButton stdSaveCancle_bt;
    private JLabel jLabel1;
    private JLabel jLabel10;
    private JLabel jLabel11;
    private JLabel jLabel12;
    private JLabel jLabel13;
    private JLabel jLabel14;
    private JLabel jLabel15;
    private JLabel jLabel16;
    private JLabel jLabel17;
    private JLabel jLabel18;
    private JLabel jLabel19;
    private JLabel jLabel2;
    private JLabel jLabel20;
    private JLabel jLabel21;
    private JLabel jLabel22;
    private JLabel jLabel23;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JPanel jPanel1;
    private JPanel jPanel10;
    private JPanel jPanel11;
    private JPanel jPanel12;
    private JPanel jPanel13;
    private JPanel jPanel14;
    private JPanel jPanel15;
    private JPanel jPanel16;
    private JPanel jPanel17;
    private JPanel jPanel18;
    private JPanel jPanel19;
    private JPanel jPanel2;
    private JPanel jPanel20;
    private JPanel jPanel21;
    private JPanel jPanel22;
    private JPanel jPanel23;
    private JPanel jPanel24;
    private JPanel jPanel25;
    private JPanel jPanel26;
    private JPanel jPanel27;
    private JPanel jPanel28;
    private JPanel jPanel29;
    private JPanel jPanel3;
    private JPanel jPanel30;
    private JPanel jPanel31;
    private JPanel jPanel32;
    private JPanel jPanel33;
    private JPanel jPanel34;
    private JPanel jPanel35;
    private JPanel jPanel36;
    private JPanel jPanel37;
    private JPanel jPanel38;
    private JPanel jPanel39;
    private JPanel jPanel4;
    private JPanel jPanel40;
    private JPanel jPanel41;
    private JPanel jPanel42;
    private JPanel jPanel43;
    private JPanel jPanel44;
    private JPanel jPanel45;
    private JPanel jPanel46;
    private JPanel jPanel47;
    private JPanel jPanel48;
    private JPanel jPanel49;
    private JPanel jPanel5;
    private JPanel jPanel50;
    private JPanel jPanel51;
    private JPanel jPanel52;
    private JPanel jPanel53;
    private JPanel jPanel54;
    private JPanel jPanel55;
    private JPanel jPanel56;
    private JPanel jPanel57;
    private JPanel jPanel58;
    private JPanel jPanel59;
    private JPanel jPanel6;
    private JPanel jPanel60;
    private JPanel jPanel7;
    private JPanel jPanel8;
    private JPanel jPanel9;
    private JTextField jTextField1;
    private JTextField jTextField10;
    private JTextField jTextField11;
    private JTextField jTextField12;
    private JTextField jTextField2;
    private JTextField jTextField3;
    private JTextField jTextField4;
    private JTextField jTextField5;
    private JTextField jTextField6;
    private JTextField jTextField7;
    private JTextField jTextField8;
    private JTextField jTextField9;
    // End of variables declaration
}
