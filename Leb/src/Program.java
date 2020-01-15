import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Program {

    static Stack<Terminal> mStack=new Stack<>();
    static Terminal t=new Terminal(0,false);
    static Map<String,Integer> imap=new HashMap<>();    //非终结符
    static Map<String,Integer> nmap=new HashMap<>();    //终结符
    static Map<Integer,String> pmap=new HashMap<>();    //产生式
    static int[][] PPT=new int[23][17];                 //预测分析表

    public static void main(String[] args){
        init();
        try{
            BufferedReader br=new BufferedReader(new FileReader("resource/input.txt"));
            File file=new File("resource/output.txt");
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fw=new FileWriter(file);
            String content;
            String nt;    //多头项指针指向的终结符
            int nti=0;      //该终结符的type

            //读取input文件内容
            while((content=br.readLine())!=null){
                if(content.contains(",")){
                    nt=content.substring(1,content.indexOf(','));
                    if(nt.equals("RELOP") || nt.equals(("BRACKET"))){
                        nt=content.substring(1,content.indexOf('>')).replaceAll(", ","/");
                    }
                }else{
                    nt=content.substring(1,content.indexOf('>'));
                }
                //得到多头项
                if(nmap.containsKey(nt)){
                    nti=nmap.get(nt);
                }else {
                    System.out.println("多头项错误");
                }

                //与栈中内容进行查找
                boolean isF=false;
                while(!isF){
                    Terminal st=mStack.pop();
                    if(st.isNon){
                        if(st.type!=nti){
                            System.out.println("查找错误");
                        }else{
                            isF=true;
                        }
                    }else{
                        //查表
                        if(PPT[nti][st.type]==-1){
                            System.out.println("不存在的表项");
                        }else{
                            String temp=pmap.get(PPT[nti][st.type]);
                            System.out.println(temp);
                            fw.write(temp+"\n");
                            String[] temps=temp.substring(temp.indexOf("->")+3).split(" ");
                            if(temps[0].equals("ε")){
                                continue;
                            }else{
                                for(int i=temps.length-1;i>=0;i--){
                                    if(imap.containsKey(temps[i])){
                                        Terminal tt=new Terminal(imap.get(temps[i]),false);
                                        mStack.push(tt);
                                    }else if(nmap.containsKey(temps[i])) {
                                        Terminal tt = new Terminal(nmap.get(temps[i]), true);
                                        mStack.push(tt);
                                    }
                                }
                            }
                        }
                    }
                }

            }

            fw.close();
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void init(){
        mStack.push(t);
        nmap.put("PUBLIC",0);
        nmap.put("CLASS",1);
        nmap.put("STATIC",2);
        nmap.put("VOID",3);
        nmap.put("STRING",4);
        nmap.put("INT",5);
        nmap.put("RELOP/LT",6);
        nmap.put("<",6);
        nmap.put("RELOP/GT",7);
        nmap.put(">",7);
        nmap.put("RELOP/ADD",8);
        nmap.put("+",8);
        nmap.put("NUM",9);
        nmap.put("ID",10);
        nmap.put("LITERAL",11);
        nmap.put("ASSIGN_OP",12);
        nmap.put("=",12);
        nmap.put("BRACKET/LRO",13);
        nmap.put("BRACKET/RRO",14);
        nmap.put("BRACKET/LSQ",15);
        nmap.put("BRACKET/RSQ",16);
        nmap.put("BRACKET/LCU",17);
        nmap.put("BRACKET/RCU",18);
        nmap.put("(",13);
        nmap.put(")",14);
        nmap.put("[",15);
        nmap.put("]",16);
        nmap.put("{",17);
        nmap.put("}",18);
        nmap.put("DOT",19);
        nmap.put(".",19);
        nmap.put("SEMICOLON",20);
        nmap.put(";",20);
        nmap.put("COMMA",21);
        nmap.put(",",21);
        nmap.put("FOR",22);

        pmap.put(1,"P -> C { F }");
        pmap.put(2,"C -> M CLASS ID");
        pmap.put(3,"C -> CLASS ID");
        pmap.put(4,"F -> M F");
        pmap.put(5,"F -> ε");
        pmap.put(6,"F -> T ID ( B ) { E }");
        pmap.put(7,"M -> PUBLIC");
        pmap.put(8,"M -> STATIC");
        pmap.put(9,"T -> VOID");
        pmap.put(10,"T -> STRING");
        pmap.put(11,"T -> INT");
        pmap.put(12,"B -> T B'");
        pmap.put(13,"B' -> [ ] ID B''");
        pmap.put(14,"B' -> ID B''");
        pmap.put(15,"B'' -> , B");
        pmap.put(16,"B'' -> ε");
        pmap.put(17,"E' -> T ID E''");
        pmap.put(18,"E' -> ID E'''");
        pmap.put(19,"E' -> ε");
        pmap.put(20,"E'' -> = NUM");
        pmap.put(21,"E'' -> ε");
        pmap.put(22,"E''' -> = E''''");
        pmap.put(23,"E''' -> > ID");
        pmap.put(24,"E''' -> < ID");
        pmap.put(25,"E'''' -> NUM");
        pmap.put(26,"E'''' -> ID E'''''");
        pmap.put(27,"E''''' -> + NUM");
        pmap.put(28,"E''''' -> ε");
        pmap.put(29,"E -> ID H");
        pmap.put(30,"E -> T ID E'' ; E");
        pmap.put(31,"E -> FOR ( E' ; E' ; E' ) { E } E");
        pmap.put(32,"E -> ε");
        pmap.put(33,"H -> E''' ; E");
        pmap.put(34,"H -> F'' ( B' ) ; E");
        pmap.put(35,"F' -> ID F''");
        pmap.put(36,"F'' -> . F'");
        pmap.put(37,"F'' -> ε");
        pmap.put(38,"B' -> LITERAL");

        imap.put("P",0);
        imap.put("C",1);
        imap.put("F",2);
        imap.put("F'",3);
        imap.put("M",4);
        imap.put("T",5);
        imap.put("B",6);
        imap.put("B'",7);
        imap.put("B''",8);
        imap.put("E",9);
        imap.put("E'",10);
        imap.put("E''",11);
        imap.put("E'''",12);
        imap.put("E''''",13);
        imap.put("E'''''",14);
        imap.put("F''",15);
        imap.put("H",16);

        for(int i=0;i<23;i++){
            for(int j=0;j<17;j++){
                PPT[i][j]=-1;
            }
        }

        PPT[0][0]=1;
        PPT[1][0]=1;
        PPT[2][0]=1;
        PPT[0][1]=2;
        PPT[1][1]=3;
        PPT[2][1]=2;
        PPT[0][2]=4;
        PPT[2][2]=4;
        PPT[3][2]=6;
        PPT[5][2]=6;
        PPT[4][2]=6;
        PPT[18][2]=5;
        PPT[10][3]=35;
        PPT[0][4]=7;
        PPT[2][4]=8;
        PPT[3][5]=9;
        PPT[4][5]=10;
        PPT[5][5]=11;
        PPT[3][6]=12;
        PPT[4][6]=12;
        PPT[5][6]=12;
        PPT[15][7]=13;
        PPT[10][7]=14;
        PPT[21][8]=15;
        PPT[11][7]=38;
        PPT[14][8]=16;
        PPT[3][9]=30;
        PPT[4][9]=30;
        PPT[5][9]=30;
        PPT[10][9]=29;
        PPT[22][9]=31;
        PPT[18][9]=32;
        PPT[3][10]=17;
        PPT[4][10]=17;
        PPT[5][10]=17;
        PPT[10][10]=18;
        PPT[14][10]=19;
        PPT[20][10]=19;
        PPT[12][11]=20;
        PPT[14][11]=21;
        PPT[20][11]=21;
        PPT[12][12]=22;
        PPT[6][12]=24;
        PPT[7][12]=23;
        PPT[10][13]=26;
        PPT[9][13]=25;
        PPT[8][14]=27;
        PPT[14][14]=28;
        PPT[20][14]=28;
        PPT[10][15]=35;
        PPT[19][15]=36;
        PPT[13][15]=37;
        PPT[10][16]=34;
        PPT[12][16]=33;
        PPT[6][16]=33;
        PPT[7][16]=33;
        PPT[19][16]=34;

    }
}
