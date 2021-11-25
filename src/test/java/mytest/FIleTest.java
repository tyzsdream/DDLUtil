//package mytest;
//
//import cn.hutool.core.util.StrUtil;
//import org.junit.Test;
//
//import java.io.File;
//import java.util.Arrays;
//
//public class FIleTest {
//    String[] xjy = new String[]{"创", "出", "利", "民", "申", "书", "士", "得", "撒上", "撒下", "王上", "王下", "代上", "代下", "拉", "尼", "斯", "伯", "诗", "箴", "传", "歌", "赛", "耶", "哀", "结", "但", "何", "珥", "摩", "俄", "拿", "弥", "鸿", "哈", "番", "该", "亚", "玛", "太", "可", "路", "约", "徒", "罗", "林前",
//            "林后", "加", "弗", "腓", "西", "帖前", "帖后", "提前", "提后", "多", "门", "来", "雅", "彼前", "彼后", "约一", "约二", "约三", "犹", "启"};
//    static int a = 0;
//
//    public static void main(String[] args) {
//        File file = new File("E:\\待处理");
//        File[] files = file.listFiles();
//        fetchFile(files, "");
//    }
//
//    public static void fetchFile(File[] files, String curPath) {
//        for (File file : files) {
//            if (file.isDirectory()) {
//                String newPath = curPath + "/" + file.getName();
//                File[] files1 = file.listFiles();
//                fetchFile(files1, newPath);
//            } else {
//                a++;
//                String path = file.getPath();
//                file.renameTo(new File(path.replace(file.getName(),String.format("%04d",a)+file.getName())));
//                System.out.println(curPath + "/" + file.getName() + "--->" + a);
//            }
//        }
//
//    }
//
////    @Test
////    public void xjy() {
////        File file = new File("E:\\待处理\\01  圣经朗读\\02");
////        File[] files = file.listFiles();
////        for (int i = 0; i < files.length; i++) {
////            File[] files1 = files[i].listFiles();
////            for (int i1 = 0; i1 < files1.length; i1++) {
////                File fileItem = files1[i1];
////                String oldPath = fileItem.getPath();
////                String name = fileItem.getName();
////                String[] split = name.trim().split("[.-]");
////                String format = String.format("%03d", Integer.parseInt(split[1]));
////                System.out.println(oldPath.replace(name, format + " " + name).replace("-", ""));
////                fileItem.renameTo(new File(oldPath.replace(name, format + " " + name).replace("-", "")));
////            }
////        }
////    }
//
//    @Test
//    public void SJG() {
//        File file = new File("E:\\待处理\\04 圣经歌己录");
//        File[] files = file.listFiles();
//        for (int i = 0; i < files.length; i++) {
//            File fileItem = files[i];
//            String oldPath = fileItem.getPath();
//            String name = fileItem.getName().replaceAll("[，,、]", "-");
//            String[] split = name.split("[0-9]");
//            String code = split[0].trim();
//            String[] split1 = name.replace(code, "").replace(",", "-").split("[.:：-]|\\s");
////            String format = String.format("%02d", Integer.parseInt(split[1]));
//            System.out.println(name + "->" + String.format("%03d", Arrays.asList(xjy).indexOf(split[0]) + 1));
//            String newName = String.format("%03d", Arrays.asList(xjy).indexOf(split[0]) + 1)
//                    + String.format("%03d", Integer.parseInt(StrUtil.isBlank(split1[0]) ? "0" : split1[0]))
//                    + String.format("%03d",Integer.parseInt( (split1[1].equalsIgnoreCase("mp3"))||StrUtil.isBlank(split1[1]) ? "0" : split1[1]))
//                    + " " + name;
//            System.out.println(oldPath.replace(fileItem.getName(), newName));
//            fileItem.renameTo(new File(oldPath.replace(fileItem.getName(), newName)));
////            fileItem.renameTo(new File(oldPath.replace(name, String.format("%02d",Arrays.asList(xjy).indexOf(split[0])) + " " + name).replace("-", "")));
//        }
//    }
//
//    @Test
//    public void AA() {
//        File file = new File("E:\\待处理\\新建文件夹");
//        File[] files = file.listFiles();
//        for (int i = 0; i < files.length; i++) {
//            File fileItem = files[i];
//            String oldPath = fileItem.getPath();
//            String name = fileItem.getName().substring(0,4);
////            String format = String.format("%02d", Integer.parseInt(split[1]));
//            System.out.println(fileItem.getPath().replaceFirst(name,name+"-"));
////            fileItem.renameTo(new File(oldPath.replace(fileItem.getName(), newName)));
//            fileItem.renameTo(new File(fileItem.getPath().replaceFirst(name,name+"-")));
//        }
//    }
//}
