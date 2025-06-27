//Dialog.java
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import pm.vo.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 *
 * @author 쌍용교육센터
 */
public class Dialog extends JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Dialog.class.getName());
    private testDTO dto;
    private test_tVO t_vo;
    int current = 0;
    List<test_ques_tVO> quesList;
    SqlSessionFactory factory;
    private test_item_tVO tit;
    ButtonGroup bg = new ButtonGroup();
    StudentFrame studentFrame;
    public Dialog(testDTO dto, StudentFrame studentFrame) {
        this.studentFrame = studentFrame;
        this.dto=dto;
        init(); //DB연결
        initComponents();
        t_ques();
        buttonlis();
        jTextField2.setText(dto.getLec_name()); //강의명
        jTextField3.setText(dto.getTest_name()); //시험명


        setVisible(true);
        this.setBounds(200,150,450,700);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    //배점, 문제번호===========================================================
    public void t_ques() {
        try{
            SqlSession ss = factory.openSession();

            quesList = ss.selectList("test.getQ", dto.getTest_idx());

            if (!quesList.isEmpty()) {
                test_ques_tVO firstQ = quesList.get(0); //첫문제 배점
                jTextField5.setText(String.valueOf(firstQ.getTest_ques_pit()));

                update_q(quesList.get(current));

                //문제번호
                t_vo = ss.selectOne("test.getT", dto.getTest_idx());
                int num = Integer.parseInt(t_vo.getTest_ques_num());
                jTextField4.setText(String.valueOf(num));
                jTextField6.setText(String.valueOf(num));


            } else {
                jTextField5.setText("0");
            }
            ss.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //문제내용, 문제수
    //문항수에 따른 버튼 비활성화=======================================================
    public void update_q(test_ques_tVO tqtvo) {//문제 업데이트
        System.out.println("Ques_idx: " + tqtvo.getQues_idx());

        jLabel9.setText(tqtvo.getQues_con()); //문제내용
        jButton1.setEnabled(current > 0);
        jLabel8.setText(String.valueOf(tqtvo.getQues_num())+"번"); //문제수
        jTextField7.setText(String.valueOf(tqtvo.getQues_num())); //문제수

        int count = Integer.parseInt(tqtvo.getTest_item_tnum()); //문항 수
        for(int i = 1; i <= 5; i++) {      //만약 문항수가 3이라면 (i <= 3), i = 1,2,3 일 때 활성화 4,5 일 때 비활성화
            boolean enable = (i <= count); {
                switch (i) {
                    case 1:
                        jRadioButton6.setEnabled(enable);
                        break;
                    case 2:
                        jRadioButton7.setEnabled(enable);
                        break;
                    case 3:
                        jRadioButton8.setEnabled(enable);
                        break;
                    case 4:
                        jRadioButton1.setEnabled(enable);
                        break;
                    case 5:
                        jRadioButton2.setEnabled(enable);
                        break;
                }
            }
        }

        //문항번호, 문항내용===================================================================
        try{
            SqlSession ss = factory.openSession();
            List<test_item_tVO> itemList = ss.selectList("item.getitem", tqtvo.getQues_idx());
            for(test_item_tVO tit : itemList) {
                if(tit.getTest_item_num().equals("1")) { //문항번호가 1이라면
                    jRadioButton6.setText(tit.getQues_con()); //문항내용 적음
                }
                else if(tit.getTest_item_num().equals("2")) {
                    jRadioButton7.setText(tit.getQues_con());
                }
                else if(tit.getTest_item_num().equals("3")) {
                    jRadioButton8.setText(tit.getQues_con());
                }
                else if(tit.getTest_item_num().equals("4")) {
                    jRadioButton1.setText(tit.getQues_con());
                }
                else if(tit.getTest_item_num().equals("5")) {
                    jRadioButton2.setText(tit.getQues_con());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //"다음"버튼을 누르면 선택했던 값 추출===================================================
    public void buttonlis() {
        jButton2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                String anw = null;
                if(jRadioButton6.isSelected()) {
                    anw = jRadioButton6.getText();
                }
                else if (jRadioButton7.isSelected()) {
                    anw = jRadioButton7.getText();
                }
                else if (jRadioButton8.isSelected()) {
                    anw = jRadioButton8.getText();
                }
                else if (jRadioButton1.isSelected()) {
                    anw = jRadioButton1.getText();
                }
                else if(jRadioButton2.isSelected()) {
                    anw = jRadioButton2.getText();
                }
                else {
                    JOptionPane.showMessageDialog(null, "답을 적어주세요.");
                    return;
                }

                //std_anw_tVO에 값 넣기
                if(anw != null) {
                    std_anw_tVO anwvo = new std_anw_tVO();
                    anwvo.setTest_idx(t_vo.getTest_idx());
                    anwvo.setQues_num(quesList.get(current).getQues_num()); //현재 질문 객체의 문제 번호
                    anwvo.setStdno(dto.getStdno());
                    anwvo.setS_anw(anw);

                    try{
                        SqlSession ss = factory.openSession();
                        ss.insert("std_anw.anw", anwvo);
                        ss.commit();
                        ss.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }                }

                //시험 번호가 전체 문제수보다 작을 때
                if(current < quesList.size() -1) {
                    current++;
                    update_q(quesList.get(current));
                }
                else if (current == quesList.size() -1 ){
                    int result = JOptionPane.showConfirmDialog(null, "마지막 문제입니다. 제출하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);


                    if(result == JOptionPane.YES_OPTION) {
                        System.out.println("제출되었습니다.");
                        try {
                            studentFrame.search2();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        dispose();
                    }
                    else{
                        return;
                    }

                }
                bg.clearSelection();
            }

        });

        //"이전"버튼을 눌렀을 때==================================================================
        jButton1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(current > 0) { //지금 시험 번호가 0보다 클 때
                    current--;
                    update_q(quesList.get(current));
                }
                else{
                    JOptionPane.showMessageDialog(null, "처음 문제입니다.");
                }
            }
        });//=====================================================================================
    }

    private void init()  {
        try {
            Reader r = Resources.getResourceAsReader("pm/config/conf.xml");
            factory = new SqlSessionFactoryBuilder().build(r);
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//==============================================================================================================

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {


        jPanel1 = new JPanel();
        jButton1 = new JButton();
        jButton2 = new JButton();
        jPanel2 = new JPanel();
        jLabel1 = new JLabel();
        jTextField2 = new JTextField();
        jTextField2.setEditable(false);
        jLabel3 = new JLabel();
        jTextField4 = new JTextField();
        jTextField4.setEditable(false);
        jTextField5 = new JTextField();
        jTextField5.setEditable(false);
        jLabel5 = new JLabel();
        jTextField3 = new JTextField();
        jTextField3.setEditable(false);
        jLabel2 = new JLabel();
        jPanel3 = new JPanel();
        jPanel4 = new JPanel();
        jTextField7 = new JTextField();
        jLabel4 = new JLabel();
        jTextField6 = new JTextField();
        jPanel5 = new JPanel();
        jPanel7 = new JPanel();
        jLabel9 = new JLabel();
        jLabel8 = new JLabel();
        jLabel9 = new JLabel();

        jPanel8 = new JPanel();
        jRadioButton6 = new JRadioButton();
        jRadioButton7 = new JRadioButton();
        jRadioButton8 = new JRadioButton();
        jRadioButton1 = new JRadioButton();
        jRadioButton2 = new JRadioButton();
        //jLabel6 = new JLabel();
        //jLabel7 = new JLabel();
        //jLabel10 = new JLabel();
        //jLabel11 = new JLabel();
        //jLabel12 = new JLabel();




        bg.add(jRadioButton1);
        bg.add(jRadioButton2);
        bg.add(jRadioButton6);
        bg.add(jRadioButton7);
        bg.add(jRadioButton8);

        bg.clearSelection();


        jButton1.setText("이전");

        jButton2.setText("다음");
        jButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("강의명:");

        jLabel3.setText("문제수:");

        jTextField4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jLabel5.setText("배점:");

        jLabel2.setText("시험명:");

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(66, 66, 66)
                                                .addComponent(jLabel2)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)))
                                .addGap(64, 64, 64)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel5))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextField4, GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                                        .addComponent(jTextField5))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3)
                                        .addComponent(jTextField4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel5)
                                        .addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2))
                                .addContainerGap(39, Short.MAX_VALUE))
        );

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 21, Short.MAX_VALUE)
        );

        jTextField7.setText("지금문항");

        jLabel4.setText("/");

        jTextField6.setText("총문제");

        GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(170, 170, 170)
                                .addComponent(jTextField7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(12)
                                .addComponent(jLabel4)
                                .addGap(12)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4)
                                        .addComponent(jTextField6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(11, Short.MAX_VALUE))
        );

        jPanel5.setLayout(new java.awt.GridLayout(2, 0));

        jLabel9.setText("문제");

        jLabel8.setText("jLabel8");

        GroupLayout jPanel7Layout = new GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
                jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel9, GroupLayout.PREFERRED_SIZE, 368, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(71, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
                jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel9, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(20, Short.MAX_VALUE))
        );

        jRadioButton6.setText("1번.");

        jRadioButton7.setText("2번.");
        jRadioButton7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jRadioButton7ActionPerformed(evt);
            }
        });

        jRadioButton8.setText("3번.");

        jRadioButton1.setText("4번.");

        jRadioButton2.setText("5번.");

        //  jLabel6.setText("jLabel6");

        //  jLabel7.setText("jLabel6");

        //  jLabel10.setText("jLabel6");

        //  jLabel11.setText("jLabel11");

        // jLabel12.setText("jLabel12");

        GroupLayout jPanel8Layout = new GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
                jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(75, 75, 75)
                                .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel8Layout.createSequentialGroup()
                                                        .addComponent(jRadioButton7)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                // .addComponent(jLabel10, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
                                        )
                                        .addGroup(jPanel8Layout.createSequentialGroup()
                                                .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(jRadioButton6)
                                                        .addComponent(jRadioButton8))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        // .addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
                                                        // .addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
                                                )
                                        )
                                )
                                .addGap(18, 18, 18)
                                .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel8Layout.createSequentialGroup()
                                                        .addGap(150)
                                                        .addComponent(jRadioButton2)
                                                        .addGap(50)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                // .addComponent(jLabel12)
                                        )
                                        .addGroup(jPanel8Layout.createSequentialGroup()
                                                        .addGap(150)
                                                        .addComponent(jRadioButton1)
                                                        .addGap(50)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                // .addComponent(jLabel11)
                                        )
                                )
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        )
        );



        jPanel8Layout.setVerticalGroup(
                jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jRadioButton6)
                                                        .addComponent(jRadioButton1)
                                                // .addComponent(jLabel6)
                                                // .addComponent(jLabel11)
                                        )
                                        .addGap(20)
                                        // .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jRadioButton7)
                                                        .addComponent(jRadioButton2)
                                                // .addComponent(jLabel10)
                                                // .addComponent(jLabel12)
                                        )
                                        .addGap(20)
                                        // .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jRadioButton8)
                                                // .addComponent(jLabel7)
                                        )
                                // .addContainerGap(77, Short.MAX_VALUE)
                        )
        );

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel7, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel8, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(jButton2, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)))
                                                .addContainerGap())
                                        .addComponent(jPanel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel8, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(jPanel5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton2, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>

    private void jButton2ActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jTextField4ActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jRadioButton7ActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
    }

    /**
     * @param args the command line arguments
     */



    // Variables declaration - do not modify
    private JButton jButton1;
    private JButton jButton2;
    private JLabel jLabel1;
    private JLabel jLabel10;
    private JLabel jLabel11;
    private JLabel jLabel12;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;

    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JPanel jPanel5;
    private JPanel jPanel7;
    private JPanel jPanel8;
    private JRadioButton jRadioButton1;
    private JRadioButton jRadioButton2;
    private JRadioButton jRadioButton6;
    private JRadioButton jRadioButton7;
    private JRadioButton jRadioButton8;
    private JTextField jTextField2;
    private JTextField jTextField3;
    private JTextField jTextField4;
    private JTextField jTextField5;
    private JTextField jTextField6;
    private JTextField jTextField7;
    // End of variables declaration
}
