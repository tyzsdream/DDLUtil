package mytest;

/**
 * <p>
 * Description:	编辑距离算法
 * 距离值：变更次数--- 先计算两个字符串的差异, str1 str2, str1要做多少次(每次一个char字符)增加 删除 替换 操作 才能与str2一致
 * 相似度：用最长的字符串的len 减去 变更次数 ,然后除以最长的字符串长度.  similarity = (maxLen - changeTimes)/maxLen
 * ORACLE函数: UTL_MATCH.EDIT_DISTANCE
 * select UTL_MATCH.EDIT_DISTANCE('Java你好','你好') from dual;
 * <／p>
 * @author duanfeixia
 * @date 2019年7月30日
 */
public class EditDistance {


    /**
     * 编辑距离算法
     * @param sourceStr 原字符串
     * @param targetStr 目标字符串
     * @return 返回最小距离: 原字符串需要变更多少次才能与目标字符串一致（变更动作：增加/删除/替换,每次都是以字节为单位）
     */
    public static int minDistance(String sourceStr, String targetStr) {
        int sourceLen = sourceStr.length();
        int targetLen = targetStr.length();

        if (sourceLen == 0) {
            return targetLen;
        }
        if (targetLen == 0) {
            return sourceLen;
        }

        //定义矩阵(二维数组)
        int[][] arr = new int[sourceLen + 1][targetLen + 1];

        for (int i = 0; i < sourceLen + 1; i++) {
            arr[i][0] = i;
        }
        for (int j = 0; j < targetLen + 1; j++) {
            arr[0][j] = j;
        }

        Character sourceChar = null;
        Character targetChar = null;

        for (int i = 1; i < sourceLen + 1; i++) {
            sourceChar = sourceStr.charAt(i - 1);

            for (int j = 1; j < targetLen + 1; j++) {
                targetChar = targetStr.charAt(j - 1);

                if (sourceChar.equals(targetChar)) {
                    /*
                     *  如果source[i] 等于target[j]，则：d[i, j] = d[i-1, j-1] + 0          （递推式 1）
                     */
                    arr[i][j] = arr[i - 1][j - 1];
                } else {
				    /*	如果source[i] 不等于target[j]，则根据插入、删除和替换三个策略，分别计算出使用三种策略得到的编辑距离，然后取最小的一个：
						d[i, j] = min(d[i, j - 1] + 1, d[i - 1, j] + 1, d[i - 1, j - 1] + 1 )    （递推式 2）
						>> d[i, j - 1] + 1 表示对source[i]执行插入操作后计算最小编辑距离
						>> d[i - 1, j] + 1 表示对source[i]执行删除操作后计算最小编辑距离
						>> d[i - 1, j - 1] + 1表示对source[i]替换成target[i]操作后计算最小编辑距离
				    */
                    arr[i][j] = (Math.min(Math.min(arr[i - 1][j], arr[i][j - 1]), arr[i - 1][j - 1])) + 1;
                }
            }
        }

//        System.out.println("----------矩阵打印---------------");
//        //矩阵打印
//        for (int i = 0; i < sourceLen + 1; i++) {
//
//            for (int j = 0; j < targetLen + 1; j++) {
//                System.out.print(arr[i][j] + "\t");
//            }
//            System.out.println();
//        }
//        System.out.println("----------矩阵打印---------------");
        return arr[sourceLen][targetLen];
    }

    /**
     * 计算字符串相似度
     * similarity = (maxlen - distance) / maxlen
     * ps: 数据定义为double类型,如果为int类型 相除后结果为0(只保留整数位)
     * @param str1
     * @param str2
     * @return
     */
    public static double getsimilarity(String str1, String str2) {
        double distance = minDistance(str1, str2);
        double maxlen = Math.max(str1.length(), str2.length());
        double res = (maxlen - distance) / maxlen;

        //System.out.println("distance="+distance);
        //System.out.println("maxlen:"+maxlen);
        //System.out.println("(maxlen - distance):"+(maxlen - distance));
        return res;
    }

    public static Integer evaluate(String str1, String str2) {
        double result = getsimilarity(str1, str2);
        return ((Double) (result * 100)).intValue();
    }

    public static void main(String[] args) {
        String s1 = "这个算法的特点是只要相似的字符串只有个别的位数是有差别变化。那这样我们可以推";
        String s2 = " 这个算法的个别特点是只要相似的字符串只有个别的位数是有差别变化。那这样我们可以推";
        String s3 = "  算法找出可以hash的key值，因为我们使用的simhash是局部敏感哈希，这个算法的特点是只要相似的字 把需要判断文本分词形成这个文章的特征单词。 最后形成去掉噪音词的只要相似的字符串只有个别的位数是有差别变化。那这样我们可以推断两个相似的文本，单词序分词是代表单词在整个句子里重要程度，数字越大越重要。  2、hash，通过hash算法把每个词变成hash值， 比如“美国”通过hash算法计算为 100101, “51区”通过hash算法计算为 101011。 这样我们的字符串就变成了一串串数字，还记得文章开头说过的吗，要把文章变为数字加权，通过 家可能会有疑问，经过这么多步骤搞这么麻烦，不就是为了得到个 0 1 字符串吗？我直接把这个文本作为字符串输入v较，前面16位变成了hash查找。后面的顺序比较的个数是多，用hd5是用于生成唯一签来相差甚远；hashmap也是用于键值对查找，便于快速插入和查找的数据结构。不过我们主要解决的是文本相似度计算，要比较的是两个文章是否相识，当然我们降维生成了hashcode也是用于这个目的。看到这里估计大家就明白了，我们使用的sim是这样的，传统hash函数解决的是生成唯一值，比如 md5、hashmap等。md5是用于生成唯一签名串，只要稍微多加一个字符md5的两个数字看起来相差甚远；hashmap也是用于键值对查找，便于快速插入和查找的数据结构。不过我们主要解决的是文本相似度计算，要比较的是两个文章是否相识，当然我们降维生成了hashcode也是用于这个目的。看到这里估计大家就明白了，我们使用的simhash就算把文章中的字符串变成 01 串也还是可以用于计算相似度的，而传统的hashcode却不行。我们可以来做个测试，两个相差只有一个字符的文本串，“你妈妈喊你回家吃饭哦，回家罗回家罗” 和 “你妈妈叫你回家吃饭啦，回家罗回家罗”。短文本大量重复信息不会被过滤，是不是";
        String s4 = "  最后形成去掉噪音词的单词序分词是代表单词在整个句子里重要程度，数字越大越重要。 最后形成去掉噪音词的单词序列并为每个词加上权重 2、hash，通过hash算法把每个词变成hash值， 比如“美国”通过hash算法计算为 100101, “51区”通过hash算法计算为 101011。 这样我们的字符串就变成了一串串数字，还记得文章开头说过的吗，分为4个16位段的存储空间是单独simhash存储空间的4倍。之前算出5000w数据是 382 Mb，扩大4倍1.5G左右，还可以接受：） 要把文章变为数字加权，通过 家可能会有疑问，经过这么多步骤搞这么麻烦，不就是为了得到个 0 1 字符串吗？我直接把这个文本作为字符串输入，用hd5是用于生成唯一签来相差甚远；hashmap也是用于键值对查找，便于快速插入和查找的数据结构。不过我们主要解决的是文本相似度计算，要比较的是两个文章是否相识，当然我们降维生成了hashcode也是用于这个目的。看到这里估计大家就明白了，我们使用的sim是这样的，传统hash函数解决的是生成唯一值，比如 md5、hashmap等。md5是用于生成唯一签名串，只要稍微多加一个字符md5的两个数字看起来相差甚远；hashmap也是用于键值对查找，便于快速插入和查找的数据结构。不过我们主要解决的是文本相似度计算，要比较的是两个文章是否相识，当然我们降维生成了hashcode也是用于这个目的。看到这里估计大家就明白了，我们使用的simhash就算把文章中的字符串变成 01 串也还是可以用于计算相似度的，而传统的hashcode却不行。我们可以来做个测试，两个相差只有一个字符的文本串，“你妈妈喊你回家吃饭哦，回家罗回家罗” 和 “你妈妈叫你回家吃饭啦，回家罗回家罗”。短文本大量重复信息不会被过滤，";
        long l3 = System.currentTimeMillis();
        System.out.println("======================================");
        System.out.println(evaluate(s1, s2));
        System.out.println(evaluate(s2, s3));
        System.out.println(evaluate(s3, s4));
        long l4 = System.currentTimeMillis();
        System.out.println(l4 - l3);
        System.out.println("======================================");
    }

}