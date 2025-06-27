import org.apache.ibatis.session.SqlSession;
import pm.vo.AdminVO;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import pm.vo.MemberVO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Reader;

public class AdminInfoDialog extends JDialog {

    SqlSessionFactory factory;
    AdminFrame parent;

    public AdminInfoDialog(AdminFrame parent, boolean modal, AdminVO adVO) {
        super(parent, modal);
        initComponents(); //화면구성


        //재윤 init 위로 올렸음 init가 아래 factory설정한것보다 아래 있으니까 factory가 null값이 나옴 그래서 위로 올림
        init(); //DB연동
        //강사 정보 출력
        jTextField1.setText(adVO.getAd_name());
        jTextField2.setText(adVO.getAdno());

        // 재윤 아이디설정***************************************************************************************
        SqlSession ss =factory.openSession();
        String str = adVO.getAdno();
        MemberVO mvo = ss.selectOne("member.adID",str);

        // 재윤 아이디설정*****************************************************************************************



        //강사 정보 출력
        jTextField1.setText(adVO.getAd_name());
        jTextField2.setText(adVO.getAdno());
        jTextField3.setText(mvo.getMem_id());  //아이디
        jTextField4.setText(adVO.getAd_phone());
        jTextField5.setText(adVO.getAd_address());
        jTextField6.setText(adVO.getAd_email());

        jTextField1.setEditable(false);
        jTextField2.setEditable(false);
        jTextField3.setEditable(false);
        jTextField4.setEditable(false);
        jTextField5.setEditable(false);
        jTextField6.setEditable(false);

        //수정 버튼 클릭 시
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //현재 버튼이 수정 상태일 때
                if(jButton1.getText().equals("수정")){
                    jButton1.setText("저장");

                    //수정 가능한 텍스트필드 활성화
                    jTextField4.setEditable(true);
                    jTextField5.setEditable(true);
                    jTextField6.setEditable(true);

                } else{ //현재 버튼이 저장 상태일 때
                    jButton1.setText("수정");

                    jTextField4.setEditable(false);
                    jTextField5.setEditable(false);
                    jTextField6.setEditable(false);

                    String ad_name = jTextField1.getText().trim();
                    String adno = jTextField2.getText().trim();
                    //String ad_id = jTextField3.getText().trim();
                    String ad_phone = jTextField4.getText().trim();
                    String ad_address = jTextField5.getText().trim();
                    String ad_email = jTextField6.getText().trim();

                    AdminVO adVO = new AdminVO();
                    adVO.setAd_name(ad_name);
                    adVO.setAdno(adno);
                    //adVO.setAd_name(ad_name); //id
                    adVO.setAd_phone(ad_phone);
                    adVO.setAd_address(ad_address);
                    adVO.setAd_email(ad_email);

                    try{
                        parent.adUpdateData(adVO);
                        JOptionPane.showMessageDialog(AdminInfoDialog.this, "수정이 완료되었습니다.");
                        dispose();
                    }catch (Exception ex){
                        JOptionPane.showMessageDialog(AdminInfoDialog.this, "수정 중 오류가 발생하였습니다.:\n" + ex.getMessage());
                        ex.printStackTrace(); // 콘솔에 에러 로그 출력 (디버깅용)
                    }


                }
            }
        });

        //취소 버튼 클릭 시
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                dispose(); //현재 창객체를 메모리상에서 삭제
            }
        });

        init(); //DB연동

        this.setBounds(300,200,450,650);
        this.setVisible(true);
        this.setTitle("강사 상세정보");

    }//생성자의 끝

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

        jPanel1 = new JPanel();
        jPanel2 = new JPanel();
        jLabel1 = new JLabel();
        jPanel3 = new JPanel();
        jButton1 = new JButton();
        jButton2 = new JButton();
        jPanel4 = new JPanel();
        jPanel5 = new JPanel();
        jLabel2 = new JLabel();
        jTextField1 = new JTextField();
        jPanel6 = new JPanel();
        jLabel3 = new JLabel();
        jTextField2 = new JTextField();
        jPanel7 = new JPanel();
        jLabel4 = new JLabel();
        jTextField3 = new JTextField();
        jPanel8 = new JPanel();
        jLabel5 = new JLabel();
        jTextField4 = new JTextField();
        jPanel9 = new JPanel();
        jLabel6 = new JLabel();
        jTextField5 = new JTextField();
        jPanel10 = new JPanel();
        jLabel7 = new JLabel();
        jTextField6 = new JTextField();

        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("맑은 고딕", 1, 18)); // NOI18N
        jLabel1.setText("강사 상세 정보");

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(152, 152, 152)
                .addComponent(jLabel1)
                .addContainerGap(159, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jButton1.setText("수정");

        jButton2.setText("취소");

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(194, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(67, 67, 67))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(50, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jPanel4.setLayout(new java.awt.GridLayout(6, 1));

        jLabel2.setText("강사명: ");

        jTextField1.setText("jTextField1");

        GroupLayout jPanel5Layout = new GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 231, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(83, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel5);

        jLabel3.setText("사번: ");

        jTextField2.setText("jTextField1");

        GroupLayout jPanel6Layout = new GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, 231, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(83, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel6);

        jLabel4.setText("아이디: ");

        jTextField3.setText("jTextField1");

        GroupLayout jPanel7Layout = new GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, 231, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(83, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel7);

        jLabel5.setText("연락처: ");

        jTextField4.setText("jTextField1");

        GroupLayout jPanel8Layout = new GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jTextField4, GroupLayout.PREFERRED_SIZE, 231, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(83, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel8);

        jLabel6.setText("주소: ");

        jTextField5.setText("jTextField1");

        GroupLayout jPanel9Layout = new GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jTextField5, GroupLayout.PREFERRED_SIZE, 231, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(83, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel9);

        jLabel7.setText("e-mail: ");

        jTextField6.setText("jTextField1");

        GroupLayout jPanel10Layout = new GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jTextField6, GroupLayout.PREFERRED_SIZE, 231, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(85, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel10);

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton jButton1;
    private JButton jButton2;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JPanel jPanel1;
    private JPanel jPanel10;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JPanel jPanel5;
    private JPanel jPanel6;
    private JPanel jPanel7;
    private JPanel jPanel8;
    private JPanel jPanel9;
    private JTextField jTextField1;
    private JTextField jTextField2;
    private JTextField jTextField3;
    private JTextField jTextField4;
    private JTextField jTextField5;
    private JTextField jTextField6;
    // End of variables declaration//GEN-END:variables
}
