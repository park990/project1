//result_Dialog
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import pm.vo.*;
import com.mysql.cj.xdevapi.Table;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hapu0
 */
public class result_Dialog extends JFrame {


    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(result_Dialog.class.getName());
    SqlSessionFactory factory;
    test_tVO testvo;
    testDTO testdto;
    static JTable[] table = new JTable[10];
    int sum;
    String grade = null;
    stdVO stdvo;
    MemberVO memberVO;
    List<result_tVO> result_list = new ArrayList<>();

    public result_Dialog(testDTO dto, String stdno) {
        testdto = dto;
        //this.memberVO = membervo;

        initComponents();
        init(); //DB 연결
        info();
        Table();

        this.setLocation(300,100);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        setVisible(true);
        this.toFront();
        this.requestFocus();

    }

    //시험이름, 강의이름 ===============================================
    public void info() {
        try {
            jLabel4.setText(testdto.getTest_name());
            jLabel2.setText(testdto.getLec_name());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //학생이름, 문제수, 학생답리스트, 정답리스트, 배점리스트 =======================================
    public void Table() {
        try{
            SqlSession ss = factory.openSession();

            stdvo = ss.selectOne("std.get_no", testdto.getStdno());
            if(stdvo != null) {
                jLabel6.setText(stdvo.getStd_name());
            }
            //====================================================================


            testvo = ss.selectOne("test.getT", testdto.getTest_idx());
            int num = Integer.parseInt(testvo.getTest_ques_num()); //문제수
            int row  = (int) Math.ceil(num / 10.0) -1;

            List<std_anw_tVO> std_anw_l  = ss.selectList("std_anw.gets_anw", testdto); //학생답 리스트
            List<test_anw_tVO> test_anw_l  = ss.selectList("test_anw.getc_anw", testdto); //정답 리스트
            List<test_ques_tVO> test_ques_l = ss.selectList("test_ques.get_ques_pit", testdto); //배점 리스트


            for(int i = 0; i < row; i++) {
                table[i] = new JTable();
            }

            //고정 테이블 코드==============================================
            int num2 = 1;
            String[] colName = new String[11];
            String[][] str1=new String[4][11];
            for(int i = 0; i < 11; i++){
                if(i == 0) {
                    colName[0] = "번호";
                    str1[0][0] = "정답";
                    str1[1][0] = "학생답";
                    str1[2][0] = "정답유무";
                    str1[3][0] = "배점";

                } else {
                    //columnName 로직 구현 - 번호 구현
                    colName[i] = String.valueOf(num2) + "번";
                    if ((num2) <= test_anw_l.size() && (num2) <= std_anw_l.size()) {
                        //정답
                        str1[0][i] = test_anw_l.get(num2 - 1).getC_anw();
                        //학생답
                        str1[1][i] = std_anw_l.get(num2 - 1).getS_anw();


                        if (str1[0][i].equals(str1[1][i])) {
                            str1[2][i] = "O";
                            int index = num2 - 1;
                            if (index < test_ques_l.size()) {
                                String pit = str1[3][i] = test_ques_l.get(index).getTest_ques_pit(); //배점
                                sum += Integer.parseInt(pit);
                            }
                        } else if (!str1[0][i].equals(str1[1][i])) {
                            str1[2][i] = "X";
                            str1[3][i] = "0";
                        } else
                            str1[2][i] = "오류";


                    }
                    num2++;
                }
            }
            //==============================================================

            //만약 JTable이 2개이상이라면 ======================================
            if(row > 0) {
                int num1 = 11;
                String[] columnName = new String[11];
                for(int q = 0; q < row; q++) {
                    String[][] str=new String[4][11];

                    for(int i = 0; i < 11; i++){
                        if(i == 0) {
                            columnName[0] = "번호";
                            str[0][0] = "정답";
                            str[1][0] = "학생답";
                            str[2][0] = "정답유무";
                            str[3][0] = "배점";
                        }
                        else {
                            //columnName 로직 구현 - 번호 구현
                            columnName[i] = String.valueOf(num1) + "번";
                            if ((num1) <= test_anw_l.size() && (num1) <= std_anw_l.size()) {
                                str[0][i] = test_anw_l.get(num1-1).getC_anw();
                                //학생답
                                str[1][i] = std_anw_l.get(num1-1).getS_anw();

                                if (str[0][i].equals(str[1][i])) {
                                    str[2][i] = "O"; //정답유무
                                    int index = num1 - 1;
                                    if (index >= 0 && index < test_ques_l.size()) {
                                        String pit = test_ques_l.get(index).getTest_ques_pit(); //배점
                                        str[3][i] = pit;
                                        sum += Integer.parseInt(pit);

                                        result_tVO result = new result_tVO();
                                        result.setQues_num(String.valueOf(num));
                                        result.setTest_idx(testdto.getTest_idx());
                                        result.setStdno(testdto.getStdno());
                                        result.setScore(String.valueOf(sum));

                                        try{
                                            SqlSession s = factory.openSession();
                                            s.commit();
                                            s.close();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    }
                                } else if (!str[0][i].equals(str[1][i])) {
                                    str[2][i] = "X";
                                    str[3][i] = "0";
                                } else
                                    str[2][i] = "오류";

                            }
                            num1++;
                        }

                    }
                    //유동 테이블 불러오기
                    table[q].setModel(new DefaultTableModel(str,columnName));
                    JScrollPane jscrollPane2 = new JScrollPane();
                    jscrollPane2.setViewportView(table[q]);
                    jPanel1.add(jscrollPane2);

                }
            }
            if(sum >= 90) {
                grade = "A";
            } else if (sum >= 80) {
                grade = "B";
            } else if (sum >= 70) {
                grade = "C";
            } else {
                grade = "D";
                //UI 이벤트 스레드가 준비되면 알림창 실행
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "보충학습 요망", "보충", JOptionPane.PLAIN_MESSAGE));
            }

            //고정 테이블 불러오기
            jTable1.setModel(new DefaultTableModel(str1, colName));
            jLabel8.setText(String.valueOf(sum +"/"+grade));


            result_tVO result = new result_tVO();
            result.setQues_num(String.valueOf(num));
            result.setTest_idx(testdto.getTest_idx());
            result.setStdno(testdto.getStdno());
            result.setScore(String.valueOf(sum));

            try{
                SqlSession s = factory.openSession();

                result_tVO exist = s.selectOne("result.re_exist", result);

                if(exist == null) {
                    s.insert("result.insert_re", result);
                    s.commit();
                    s.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //DB연결 =======================================================
    private void init() {
        try {
            Reader r = Resources.getResourceAsReader("pm/config/conf.xml");
            factory = new SqlSessionFactoryBuilder().build(r);
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//================================================================================
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jPanel1.setLayout(new java.awt.GridLayout(6, 0));

        jLabel1.setText("강의명:");

        jLabel2.setText("강의명");

        jLabel3.setText("시험명:");

        jLabel4.setText("시험명");

        jLabel5.setText("이름:");

        jLabel6.setText("jLabel6");

        jLabel7.setText("점수/등급:");

        jLabel8.setText("jLabel8");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel8))
                                .addContainerGap(37, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null, null, null, null}
                },
                new String [] {
                        "번호", "1번", "2번", "3번", "4번", "5번", "6번", "7번", "8번", "9번", "10번"
                }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 638, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        //java.awt.EventQueue.invokeLater(() -> new result_Dialog().setVisible(true));
    }

    // Variables declaration - do not modify
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;

    public void setModal(boolean b) {
    }
    // End of variables declaration
}
