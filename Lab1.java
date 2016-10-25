package lab;
//checkstyle之后的代码
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class EvaExpression {

  public static void main(String[] args) {
    String nstr = "";
    System.out.println("输入'Exit'退出程序！");
    Scanner n = new Scanner(System.in);
    ProExpression exp = new ProExpression();
    while (true) {
      nstr = n.nextLine();// 输入
      if ("Exit".equals(nstr)) {
        break;
      }
      long starTime = System.currentTimeMillis();// 测试程序运行时间
      exp.ProExp(nstr);
      System.out.println("程序开始时间：" + starTime + "毫秒");//
      long endTime = System.currentTimeMillis();//
      System.out.println("程序结束时间：" + endTime + "毫秒");//
      long Time = endTime - starTime;//
      System.out.println("程序执行时间：" + Time + "毫秒");//
    }
    n.close();
  }

}

class ProExpression {
  String MemStr = ""; // 存储合法的表达式
  String Derstr = ""; // 存储求导的结果
  String Estr = ""; // 判断表达式输入的结果

  // 整理表达式
  private String StaExp(String str) {
    String sEstr;
    sEstr = str;
    int j = 0;
    while (j != -1) {// 把x^2转换为x*x等
      j = sEstr.indexOf('^');
      if (j != -1) {
        String str0 = sEstr.substring(j - 1, j);
        String str1 = "";
        int i;
        for (i = j + 1; i < sEstr.length(); i++) {
          if (!(sEstr.charAt(i) >= 48 && sEstr.charAt(i) <= 57)) {
            break;
          }
        }
        String str2 = "";
        str2 = sEstr.substring(j + 1, i);
        int h = Integer.valueOf(str2).intValue();
        for (int k = 0; k < h; k++) {
          str1 = str1 + str0 + "*";
        }
        sEstr = sEstr.substring(0, j - 1) + str1.substring(0, str1.length() - 1) + sEstr.substring(i);
      }
    }
    String rstr = "";// 把系数提出来，变量按序排列
    String[] sEstrArray = sEstr.split("\\+");
    String[][] Array = new String[sEstrArray.length][2];
    for (int k = 0; k < sEstrArray.length; k++) {
      String[] _sEstrArray = sEstrArray[k].split("\\*");
      float num = 1;
      String str0 = "";
      String[] str1 = new String[_sEstrArray.length];
      int m = 0;
      for (int l = 0; l < _sEstrArray.length; l++) {
        char ch = _sEstrArray[l].charAt(0);
        if ((ch >= '0' && ch <= '9') || ch == '-') {
          num = num * Float.valueOf(_sEstrArray[l]).floatValue();
        } else {
          str1[m++] = _sEstrArray[l];
        }
      }
      Array[k][0] = String.valueOf(num);
      if (m > 0) {
        String[] str2 = new String[m];
        for (int n = 0; n < m; n++) {
          str2[n] = str1[n];
        }
        Arrays.sort(str2);
        for (int n = 0; n < m; n++) {
          str0 += str2[n] + "*";
        }
        Array[k][1] = str0.substring(0, str0.length() - 1);
      } else {
        Array[k][1] = "";
      }
    }
    for (int k = 0; k < sEstrArray.length - 1; k++) {
      for (int l = k + 1; l < sEstrArray.length; l++) {
        if (Array[k][1].equals(Array[l][1])) {
          Array[k][0] = String
              .valueOf(Float.valueOf(Array[k][0]).floatValue() + Float.valueOf(Array[l][0]).floatValue());
          Array[l][1] = "#";
        }
      }
    }
    for (int k = 0; k < sEstrArray.length; k++) {
      if (Array[k][1] != "#") {
        if (Array[k][1] != "") {
          rstr += Array[k][0] + "*" + Array[k][1] + "+";
        } else {
          rstr += Array[k][0] + "+";
        }
      }
    }
    return rstr.substring(0, rstr.length() - 1);
  }

  // 判断表达式是否正确
  private boolean expression(String str) {
    str = str.replace(" ", "");
    str = str.replace(" ", "");
    boolean result;
    int flag = 0;
    int i = 0;
    char ch0, ch1, ch2;
    if (str.length() == 0) {
      flag = 1;
    } else if (str.length() == 1) {// 单字符
      char ch = str.charAt(0);
      if (!((ch >= 48 && ch <= 57) || (ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122))) {
        flag = 1;
      }
    } else {
      for (i = 0; i < str.length() - 1; i++) {
        ch0 = str.charAt(i);
        ch1 = str.charAt(i + 1);
        if (ch0 == 42 || ch0 == 43) {// *+
          if (i == 0 || (!((ch1 >= 48 && ch1 <= 57) || (ch1 >= 65 && ch1 <= 90) || (ch1 >= 97 && ch1 <= 122)))) {
            flag = 1;
            break;
          }
        } else if (ch0 == 45) {// -
          if (i == 0 || (!((ch1 >= 48 && ch1 <= 57) || (ch1 >= 65 && ch1 <= 90) || (ch1 >= 97 && ch1 <= 122)))) {
            flag = 1;
            break;
          } else {
            str = str.substring(0, i) + "+" + str.substring(i);
            i += 1;
          }
        } else if (ch0 == 94) {// ^
          if (i == 0) {
            flag = 1;
            break;
          } else {
            char ch_1 = str.charAt(i - 1);
            if (!(((ch_1 >= 65 && ch_1 <= 90) || (ch_1 >= 97 && ch_1 <= 122) || (ch_1 >= 48 && ch_1 <= 57))
                && (ch1 >= 48 && ch1 <= 57))) {
              flag = 1;
              break;
            }
          }
        } else if (ch0 >= 48 && ch0 <= 57) {// 3x，300，3+/*/-/^
          if ((ch1 >= 48 && ch1 <= 57) || (ch1 == 42 || ch1 == 43 || ch1 == 45 || ch1 == 94)) {
            str = str.substring(0, i + 1) + "" + str.substring(i + 1);
          } else if ((ch1 >= 65 && ch1 <= 90) || (ch1 >= 97 && ch1 <= 122)) {
            str = str.substring(0, i + 1) + "*" + str.substring(i + 1);
          } else {
            flag = 1;
            break;
          }
        } else if ((ch0 >= 65 && ch0 <= 90) || (ch0 >= 97 && ch0 <= 122)) {// x3
                                                                           // xy
          if ((ch1 >= 48 && ch1 <= 57) || (ch1 >= 65 && ch1 <= 90) || (ch1 >= 97 && ch1 <= 122)) {
            str = str.substring(0, i + 1) + "*" + str.substring(i + 1);
          } else if (ch1 == 42 || ch1 == 43 || ch1 == 45 || ch1 == 94) {
            str = str.substring(0, i + 1) + "" + str.substring(i + 1);
          } else {
            flag = 1;
            break;
          }
        } else {
          flag = 1;
          break;
        }
      }
      ch2 = str.charAt(str.length() - 1);
      if (ch2 == 42 || ch2 == 43 || ch2 == 45 || ch2 == 94) {
        flag = 1;
      }
    }
    if (flag == 1) {
      Estr = "Expressions is not valid!";
      result = false;
    } else {
      Estr = str;
      Estr = StaExp(Estr);
      result = true;
    }
    return result;
  }

  // 用数值替换相应的变量
  private String simplify(String str) {
    int i;
    int flag = 0;
    String sstr = MemStr;
    String[] strArray = str.split(" ");
    if (strArray.length > 1) {
      for (i = 1; i < strArray.length; i++) {
        String[] _strArray = strArray[i].split("=");
        int k = sstr.indexOf(_strArray[0]);
        if (k != -1) {
          sstr = sstr.replace(_strArray[0], _strArray[1]);
        } else {
          System.out.println("Variable Error!");
          flag = 1;
          break;
        }
      }
    }
    if (flag == 1) {
      return "";
    } else {
      return StaExp(sstr);
    }
  }

  // 求导
  private boolean derivative(String str) {
    Derstr = "";
    int i;
    int cnt = 0;
    String[] strArray = str.split(" ");
    String[] cstrArray = MemStr.split("\\+");
    for (i = 0; i < cstrArray.length; i++) {
      String Estr = "";
      int k = cstrArray[i].indexOf(strArray[1]);
      if (k != -1) {
        int cnt1 = 0;
        for (int j = 0; j < cstrArray[i].length(); j++) {// 计算变量个数，确定系数
          if (cstrArray[i].charAt(j) == strArray[1].charAt(0)) {
            cnt1 += 1;
          }
        }
        Estr = cstrArray[i].substring(0, k) + cstrArray[i].substring(k + 1);
        if (cstrArray[i].charAt(k) == '*') {
          Estr = Estr.substring(0, k) + Estr.substring(k + 1);
        } else if (cstrArray[i].charAt(k - 1) == '*') {
          Estr = Estr.substring(0, k - 1) + Estr.substring(k);
        }
        if (cnt1 > 1) {
          Estr = Estr + "*" + String.valueOf(cnt1);
        }
        Derstr += Estr;
        Derstr += "+";
      } else {
        cnt += 1;
      }
    }
    if (cnt == cstrArray.length) {
      System.out.println("Variable Error!");
      return false;
    } else {
      Derstr = Derstr.substring(0, Derstr.length() - 1);
      Derstr = StaExp(Derstr);
      return true;
    }
  }

  // 判断输入是 简化命令/求导命令/表达式？
  public void ProExp(String str) {
    boolean result1 = Pattern.matches("!simplify( [a-z | A-Z]=-{0,1}[0-9]{0,}\\.{0,1}[0-9]*)*", str);
    boolean result2 = Pattern.matches("!d/d [a-z | A-Z]", str);
    if (result1 == true) {
      String str0 = simplify(str);
      if (!str0.equals("")) {
        System.out.println(str0);
      }
    } else if (result2 == true) {
      if (derivative(str)) {
        System.out.println(Derstr);
      }
    } else {
      boolean r = expression(str);
      if (r == true) {
        this.MemStr = this.Estr;
        System.out.println(MemStr);
      } else {
        System.out.println(Estr);
      }
    }
  }
}
