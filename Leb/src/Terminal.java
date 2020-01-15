/**
 * 终结符和非终结符类
 * type：标识符号
 * isNon：标识是否为结束符
 */
public class Terminal {
    int type;
    boolean isNon;
    Terminal(int t, boolean n){
        type=t;
        isNon=n;
    }
}
