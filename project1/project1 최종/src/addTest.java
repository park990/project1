import pm.vo.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.Reader;
import java.util.List;

public class addTest extends JFrame {


    private SqlSessionFactory factory;
    private String[] c_name ={"문항"};
    private String[][] items;
    private int qnum_cnt;
    private List<test_quesVO> q_list;
    List<test_itemVO> i_list;
    DefaultTableModel model;


    public addTest(TestVO tvo , int num , boolean state , List<test_quesVO> list) { // 생성자 시작
        items = new String[5][c_name.length];
        init();// 공장 생성
        initComponents(); // 화면 생성
        qnum_cnt = num;
        qnum_tf.setText(String.valueOf(qnum_cnt));

        if(!state){// 문제가 다 완성 되었는지 상태에 따라 실행 - 문제를 다 완성했을 때만 이쪽으로 와서 실행
            // 문제 내용, 문항, 배점 잠구기
            ques_ta.setEditable(state);
            table.setEnabled(state);
            test_ques_pit_tf.setEditable(state);


            //마지막 번호 문제 내용, 문항, 배점 받아오기
            q_list = list;// 문제들 리스트에 받아옴
            test_quesVO qvo = q_list.get(num-1);

            //문제 내용 세팅
            ques_ta.setText(qvo.getQues_con());

            //문항 세팅이 개빡이네
            //문항 리스트로 받아오고 순서대로 세팅
            SqlSession ss = factory.openSession();
            i_list = ss.selectList("test_item.call_items",q_list.get(Integer.parseInt(qnum_tf.getText())-1));
            ss.close();
            items = new String[5][c_name.length];
            int i=0;
            for(test_itemVO ivo: i_list){
                items[i][0] = ivo.getQues_con();
                i++;
            }

            table.setModel(model = new DefaultTableModel(items, c_name));
            //배점 세팅
            test_ques_pit_tf.setText(qvo.getTest_ques_pit());

            // 저장 버튼 수정 버튼으로 변경
            save_ques_bt.setText("수정");

        }

        setTitle("문제 만들기");
        tname_tf.setText(tvo.getTest_name());
        tques_tf.setText(tvo.getTest_ques_num());

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        cancel_ques_bt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        save_ques_bt.addActionListener(new ActionListener() { // 저장 버튼을 눌렀을 때
            @Override
            public void actionPerformed(ActionEvent e) {
                SqlSession ss = factory.openSession(true);
                if (save_ques_bt.getText().equals("저장")){//저장버튼 눌렀을 때
                    String tname = new String(); // 시험 이름 받기
                    String tques = new String(); // 시험 문제 수 받기
                    String qnum = new String();  // 문제 번호 받기
                    String ques = new String();  // 시험 내용 받기
                    String anwnum = new String(); // 문항 답 번호 받기
                    String pit = new String();  // 배점 받기
                    StringBuffer sb = new StringBuffer();
                    int cnt = 0;
                    int item_tnum = 0;//사용한 문항 수


                    //문항 내용 수 계산 -> 문제의 문항 수에 들어가는 값이라 먼저 계산해 줌
                    // 문항을 적었다가 지우기만 햇을 경우 그 행이 존재하는 걸로 잡혀서 그것을 안 쓰는 행으로 초기화 하는 문장
                    //입력된 문항 수 계산
                    for (int row = 0; table.getRowCount() > row; row++) {
                        if( table.getValueAt(row, 0)!=null){
                            String str = table.getValueAt(row, 0).toString();
                            if (str != null && !str.trim().isEmpty()) {
                                item_tnum++;
                            }
                        }
                    }




                    if (tname_tf.getText().length() > 0) {
                        tname = tname_tf.getText().trim(); // 시험 이름 받기
                    } else {
                        cnt++;
                        sb.append("시험명, ");
                    }
                    if (tques_tf.getText().length() > 0) {
                        tques = tques_tf.getText().trim(); // 시험 문제 수 받기
                    } else {
                        cnt++;
                        sb.append("총 문제 수, ");
                    }
                    if (qnum_tf.getText().length() > 0) {
                        qnum = qnum_tf.getText().trim();  // 문제 번호 받기
                    } else {
                        cnt++;
                        sb.append("문제 번호, ");
                    }
                    if (ques_ta.getText().length() > 0) {
                        ques = ques_ta.getText().trim();  //  시험 내용 받기
                    } else {
                        cnt++;
                        sb.append("문제 내용, ");
                    }
                    if (item_tnum > 0) { // 문항이 들어왔을 때

                    } else { // 문항이 들어오지 않았을 때
                        cnt++;
                        sb.append("문항, ");
                    }

                    if (test_ques_pit_tf.getText().length() > 0) {
                        pit = test_ques_pit_tf.getText().trim();// 배점 받기
                    } else {
                        cnt++;
                        sb.append("배점, ");
                    }
                    if (anwnum_tf.getText().length() > 0) {
                        anwnum = anwnum_tf.getText().trim(); // 문항 답 번호 받기
                    } else {
                        cnt++;
                        sb.append("정답 번호");
                    }


                    if (cnt > 0) {
                        sb.append("을(를) 입력해주세요!");
                        JOptionPane.showMessageDialog(addTest.this, sb.toString());
                    } else {
                        test_itemVO voi = new test_itemVO();



                        //시험지 저장 - 안해도 될걸?
                        //TestVO vo = new TestVO();
                        //vo.setLec_no(esVO.getLec_no());// 여긴 강의에서 받아오는 거
                        //vo.setTest_name(tname);
                        //vo.setTest_ques_num(tques);

                        //ss.insert("test.save", vo);// 이때 vo에 다시 알아서 test_idx가 저장됨

                        //문제 저장
                        test_quesVO voq = new test_quesVO();
                        voq.setQues_num(qnum);
                        voq.setQues_con(ques);
                        voq.setTest_item_tnum(String.valueOf(item_tnum));
                        voq.setTest_ques_pit(pit);
                        voq.setTest_idx(tvo.getTest_idx());

                        int q_save_cnt =ss.insert("test_ques.save", voq); // 이때 voq에 ques_idx가 저장됨
                        if(q_save_cnt>0){
                            ss.commit();
                        }else {
                            ss.rollback();
                        }
                        //입력된 문항 수에 따른 문항 저장문
                        for (int row = 0; table.getRowCount() > row; row++) {
                            if( table.getValueAt(row, 0)!=null) {
                                String str = table.getValueAt(row, 0).toString();
                                if (str != null && !str.trim().isEmpty()) {
                                    //문항 저장
                                    voi.setQues_idx(voq.getQues_idx()); // 문제 코드를 애초에 삽입해야 할 듯
                                    voi.setTest_item_num(String.valueOf(row+1));
                                    voi.setQues_con(table.getValueAt(row, 0).toString());
                                    int i_save_cnt = ss.insert("test_item.save", voi);
                                    if (i_save_cnt > 0) {
                                        ss.commit();
                                    } else {
                                        ss.rollback();
                                    }
                                }
                            }
                        }


                        //답안 저장
                        test_anwVO voa = new test_anwVO();
                        voa.setTest_idx(tvo.getTest_idx()); // 시험지 코드를 애초에 삽입 해야할 듯
                        voa.setQues_num(qnum);
                        voa.setC_anw(table.getValueAt(table.getSelectedRow(), 0).toString());

                        int a_save_cnt=ss.insert("test_anw.save", voa);
                        if(a_save_cnt>0){
                            ss.commit();
                        }else{
                            ss.rollback();
                        }


                        if (tques.equals(qnum)) { // 총 문제 수랑 현재 문제 번호가 같을 때 저장하면 끝!
                            dispose();
                        }

                        qnum_cnt++;//문제 번호 증가
                        qnum_tf.setText(String.valueOf(qnum_cnt));
                        // 문제 내용 초기화
                        ques_ta.setText("");
                        // 테이블 초기화
                        items = new String[5][];
                        table.setModel(new DefaultTableModel(items, c_name));
                        // 배점 초기화
                        test_ques_pit_tf.setText("");

                    }

                }else if(save_ques_bt.getText().equals("수정")){// 수정 버튼을 눌렀을 때
                    ques_ta.setEditable(true);
                    table.setEnabled(true);
                    test_ques_pit_tf.setEditable(true);
                    qnum_tf.setEditable(true);
                    save_ques_bt.setText("변경");
                }else { //변경버튼을 눌렀을때
                    String tname = new String(); // 시험 이름 받기
                    String tques = new String(); // 시험 문제 수 받기
                    String qnum = new String();  // 문제 번호 받기
                    String ques = new String();  // 시험 내용 받기
                    String anwnum = new String(); // 문항 답 번호 받기
                    String pit = new String();  // 배점 받기
                    StringBuffer sb = new StringBuffer();
                    int cnt = 0;
                    int item_tnum = 0;//사용한 문항 수

                    //오류 발생하는 곳
                    /*for (int row = 0; table.getRowCount() > row; row++) {
                        System.out.println(row);
                        if (table.getValueAt(row, 0).toString().trim().equals("")) {
                            System.out.println(table.getValueAt(row,0));
                            model.setValueAt(null, row, 0);
                        }
                    }*/

                    for (int row = 0; table.getRowCount() > row; row++) {
                        if( table.getValueAt(row, 0)!=null) {
                            String str = table.getValueAt(row, 0).toString();
                            if (str != null && !str.trim().isEmpty()) {
                                item_tnum++;
                            }
                        }
                    }

                    if (tname_tf.getText().length() > 0) {
                        tname = tname_tf.getText().trim(); // 시험 이름 받기
                    } else {
                        cnt++;
                        sb.append("시험명, ");
                    }
                    if (tques_tf.getText().length() > 0) {
                        tques = tques_tf.getText().trim(); // 시험 문제 수 받기
                    } else {
                        cnt++;
                        sb.append("총 문제 수, ");
                    }
                    if (qnum_tf.getText().length() > 0) {
                        qnum = qnum_tf.getText().trim();  // 문제 번호 받기
                    } else {
                        cnt++;
                        sb.append("문제 번호, ");
                    }
                    if (ques_ta.getText().length() > 0) {
                        ques = ques_ta.getText().trim();  //  시험 내용 받기
                    } else {
                        cnt++;
                        sb.append("문제 내용, ");
                    }
                    if (item_tnum > 0) { // 문항이 들어왔을 때

                    } else { // 문항이 들어오지 않았을 때
                        cnt++;
                        sb.append("문항, ");
                    }

                    if (test_ques_pit_tf.getText().length() > 0) {
                        pit = test_ques_pit_tf.getText().trim();// 배점 받기
                    } else {
                        cnt++;
                        sb.append("배점, ");
                    }
                    if (anwnum_tf.getText().length() > 0) {
                        anwnum = anwnum_tf.getText().trim(); // 문항 답 번호 받기
                    } else {
                        cnt++;
                        sb.append("정답 번호");
                    }

                    if (cnt > 0) {
                        sb.append("을(를) 입력해주세요!");
                        JOptionPane.showMessageDialog(addTest.this, sb.toString());
                    } else {

                        //해당 번호의 문제를 가져옴
                        test_quesVO qvo = q_list.get(Integer.parseInt(qnum_tf.getText()) - 1);
                        //해당 문제의 문항들 싹 다 지워 버리기
                        int i_del_cnt = ss.delete("test_item.del_items", qvo.getQues_idx());
                        if (i_del_cnt > 0) {
                            ss.commit();
                        } else {
                            ss.rollback();
                        }
                        //새로 변경된 문항들 다 저장하기
                        test_itemVO ivo = new test_itemVO();
                        int row = 0;
                        System.out.println(table.getRowCount());
                        for (row = 0; table.getRowCount() > row; row++) {
                            if(table.getValueAt(row, 0)!=null) {
                                if (table.getValueAt(row, 0).toString().trim().length() > 0) {
                                    //문항 저장
                                    ivo.setQues_idx(qvo.getQues_idx()); // 문제 코드를 애초에 삽입해야 할 듯
                                    ivo.setTest_item_num(String.valueOf(row + 1));
                                    ivo.setQues_con(table.getValueAt(row, 0).toString());
                                    ss.insert("test_item.save", ivo);
                                }
                            }
                        }
                        //문제의 변경사항이 변경 사항 수가 있는지 확인하는 int자료형
                        int change_cnt = 0;
                        test_quesVO c_qvo = new test_quesVO();
                        c_qvo.setTest_idx(qvo.getTest_idx());
                        c_qvo.setQues_num(qnum_tf.getText());
                        //새로 변경된 문항들이 늘어나거나 줄어들었을 수 잇으니 문제테이블이 갖고 있는 문항 수도 변경해주셈!
                        if (qvo.getTest_item_tnum().equals(String.valueOf(row))) {// 변경 전 문제의 총 문항 수와 변경 된 문항의 수가 같다면
                            System.out.println("row"+row);
                            System.out.println("문항수 같음");
                            c_qvo.setTest_item_tnum(String.valueOf(item_tnum));
                        } else {// 변경 전 문제의 총 문항 수와 변경 된 문항의 수가 다르다면
                            //문제테이블의 문항수 변경!
                            System.out.println("문항수 변경");
                            change_cnt++;
                            c_qvo.setTest_item_tnum(String.valueOf(item_tnum));
                        }
                        //문제 내용이랑 배점도 변경 확인하고 데이터 변경해야됨.
                        if (qvo.getQues_con().equals(ques_ta.getText().trim())) { // 문제 내용이 변경 전과 후가 같다면
                            System.out.println("문제내용 같음");
                            c_qvo.setQues_con(ques_ta.getText().trim());
                        } else {//문제 내용이 변경 전과 후가 다르다면
                            System.out.println("문제 내용 변경");
                            change_cnt++;
                            c_qvo.setQues_con(ques_ta.getText().trim());
                        }

                        if (qvo.getTest_ques_pit().equals(test_ques_pit_tf.getText().trim())) {// 배점이 변경 전과 후가 같다면
                            c_qvo.setTest_ques_pit(test_ques_pit_tf.getText().trim());
                            System.out.println("배점 같음");
                        } else {
                            System.out.println("배점 변경");
                            change_cnt++;
                            c_qvo.setTest_ques_pit(test_ques_pit_tf.getText().trim());
                        }
                        System.out.println("change_cnt:"+change_cnt);
                        if (change_cnt > 0) { //변경 사항이 하나라도 있을 경우
                            int update_cnt = ss.update("test_ques.change_ques", c_qvo);
                            System.out.println("update_cnt:"+update_cnt);
                            if (update_cnt > 0) {
                                ss.commit();
                            } else {
                                ss.rollback();
                            }
                        }
                        //답도 변경 될 수 있음!
                        //시험에 해당하는 답안을 지워버리기
                        int d_anw_cnt = ss.delete("test_anw.del_anw",c_qvo);
                        if (d_anw_cnt > 0) {
                            ss.commit();
                        } else {
                            ss.rollback();
                        }
                        // 변경 버튼을 눌렀을 때 답안으로 저장하기
                        test_anwVO change_avo = new test_anwVO();
                        change_avo.setTest_idx(tvo.getTest_idx());
                        change_avo.setQues_num(qnum_tf.getText().trim());
                        change_avo.setC_anw(table.getValueAt(table.getSelectedRow(), 0).toString());
                        ss.insert("test_anw.save", change_avo);
                    }
                }
                ss.close();
            }

        }); // 저장 버튼 끝

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(table.getValueAt(table.getSelectedRow(),0)!=null) {
                    String test = table.getValueAt(table.getSelectedRow(), 0).toString().trim(); //클릭한 테이블의 내용을 가져옴

                    // 값이 있는 행을 선택했을 경우
                    if (test.length() > 0) {
                        anwnum_tf.setText(String.valueOf(table.getSelectedRow()+1));
                    }


                }else {
                    //값이 없는 행을 선택했을 경우
                    anwnum_tf.setText("");
                }
            }
        });

        qnum_tf.addActionListener(new ActionListener() {//문제 번호를 고치고 엔터 눌렀을 때
            @Override
            public void actionPerformed(ActionEvent e) {
                test_quesVO qvo = q_list.get(Integer.parseInt(qnum_tf.getText())-1);
                SqlSession ss = factory.openSession();
                i_list = ss.selectList("test_item.call_items",qvo);

                ss.close();
                items = new String[5][c_name.length];
                int i=0;
                for(test_itemVO ivo: i_list){
                    items[i][0] = ivo.getQues_con();
                    i++;
                }
                //문제내용 세팅
                ques_ta.setText(qvo.getQues_con());

                //테이블 세팅
                table.setModel(new DefaultTableModel(items, c_name));

                //배점 세팅
                test_ques_pit_tf.setText(qvo.getTest_ques_pit());

            }
        });//문제 번호 누르고 엔터 눌렀을 때 끝


        setVisible(true);
    }// 생성자 끝

    public void init() {
        try{
            Reader r = Resources.getResourceAsReader("pm/config/conf.xml");
            factory = new SqlSessionFactoryBuilder().build(r);
            r.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initComponents() {

        north_p = new JPanel();
        jLabel1 = new JLabel();
        tname_tf = new JTextField();
        jLabel2 = new JLabel();
        qnum_tf = new JTextField();
        center_p = new JPanel();
        jScrollPane1 = new JScrollPane();
        ques_ta = new JTextArea();
        south_p = new JPanel();
        items_p = new JPanel();
        jScrollPane2 = new JScrollPane();

        save_p = new JPanel();
        jPanel2 = new JPanel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jLabel5 = new JLabel();
        anwnum_tf = new JTextField();
        test_ques_pit_tf = new JTextField();
        tques_tf = new JTextField();
        jPanel1 = new JPanel();
        save_ques_bt = new JButton();
        cancel_ques_bt = new JButton();
        table = new JTable();

        setPreferredSize(new java.awt.Dimension(350, 600));

        north_p.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        //시험명 관련 시작 ----------------------
        jLabel1.setText("시험명:");
        north_p.add(jLabel1);

        tname_tf.setColumns(8);
        tname_tf.setEditable(false);
        north_p.add(tname_tf);
        //시험명 관련 끝 ----------------------

        //총문제 관련 시작 ----------------------
        jLabel4.setText("총 문제:");
        north_p.add(jLabel4);

        tques_tf.setColumns(2);
        tques_tf.setEditable(false);
        north_p.add(tques_tf);
        //총문제 관련 끝 ----------------------

        //문제 번호 관련 시작 ----------------------
        jLabel2.setText("문제 번호:");
        north_p.add(jLabel2);

        qnum_tf.setColumns(2);

        qnum_tf.setEditable(false);
        north_p.add(qnum_tf);
        //문제 번호 관련 끝 ----------------------

        getContentPane().add(north_p, java.awt.BorderLayout.PAGE_START);

        center_p.setPreferredSize(new java.awt.Dimension(529, 100));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(244, 244));

        ques_ta.setColumns(20);
        ques_ta.setRows(5);
        ques_ta.setPreferredSize(new java.awt.Dimension(232, 232));
        jScrollPane1.setViewportView(ques_ta);

        center_p.add(jScrollPane1);

        getContentPane().add(center_p, java.awt.BorderLayout.CENTER);

        south_p.setPreferredSize(new java.awt.Dimension(50, 190));
        south_p.setLayout(new java.awt.GridLayout(2, 0));

        items_p.setPreferredSize(new java.awt.Dimension(40, 150));
        items_p.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setPreferredSize(new java.awt.Dimension(49, 150));


        table.setModel(new DefaultTableModel(items,c_name));
        JScrollPane scrollPane = new JScrollPane(table);


        items_p.add(scrollPane, java.awt.BorderLayout.CENTER);

        south_p.add(items_p);

        save_p.setLayout(new java.awt.GridLayout(2, 0));

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jLabel5.setText("배점:");
        jPanel2.add(jLabel5);

        test_ques_pit_tf.setColumns(2);
        jPanel2.add(test_ques_pit_tf);


        jLabel3.setText("정답 번호:");
        jPanel2.add(jLabel3);

        anwnum_tf.setColumns(2);
        anwnum_tf.setEditable(false);
        jPanel2.add(anwnum_tf);

        save_p.add(jPanel2);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        save_ques_bt.setText("저장");
        jPanel1.add(save_ques_bt);

        cancel_ques_bt.setText("취소");
        jPanel1.add(cancel_ques_bt);

        save_p.add(jPanel1);

        south_p.add(save_p);

        getContentPane().add(south_p, java.awt.BorderLayout.PAGE_END);
        south_p.getAccessibleContext().setAccessibleName("");

        pack();
    }

    private JTextField anwnum_tf;
    private JButton cancel_ques_bt;
    private JPanel center_p;

    private JPanel items_p;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JPanel north_p;
    private JTextField qnum_tf; // 문제 번호
    private JTextArea ques_ta; // 문제 내용
    private JPanel save_p;
    private JButton save_ques_bt;// 저장 버튼
    private JPanel south_p;
    private JTextField tname_tf; //시험이름
    private JTextField tques_tf; //총 문제 수
    private JTextField test_ques_pit_tf; // 배점
    private JTable table;
}

