import com.forte.demo.robot.utils.LogicUtil;

/**
 * @author 陈瑞扬
 * @date 2019年12月08日 12:21
 * @description
 */
public class functionTest {

    // 获取指定范围的随机数
    public  int getRandom(int intBegin,int intEnd){
        Double douJrrp = Math.random() * (intBegin-intBegin-1)+1;
        int intJrrp = douJrrp.intValue();
        intJrrp+=intBegin;
        return intJrrp;

    }

//    public static void main(String[] args) {
//        functionTest test = new functionTest();
//
//        for (int i = 0; i < 1000; i++) {
//            test.getRandom(15,90);
//        }
//
//
//    }
}
