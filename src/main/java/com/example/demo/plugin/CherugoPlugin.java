package com.example.demo.plugin;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import net.lz1998.cq.event.message.CQGroupMessageEvent;
import net.lz1998.cq.robot.CQPlugin;
import net.lz1998.cq.robot.CoolQ;

/**
 * 切噜语的编码和解码 切噜词以’切‘开头，字符集为CHERU_SET cheruWord = ‘切’ ^ 'CHERU_SET'
 * 
 * 切噜语由切噜词和标点符号链接而成 cheruLanguage = cheruWord + \\W
 */
@Component
public class CherugoPlugin extends CQPlugin {
    // 切噜语的编码和解码

    String msg;

    // 切噜语字符集
    String CHERU_SET = "切卟叮咧哔唎啪啰啵嘭噜噼巴拉蹦铃";
    String ENCODING = "GB18030";
    // 词分割
    Pattern rexSplit = Pattern.compile("\\b");
    // 匹配汉字
    Pattern rexWord = Pattern.compile("[\u4e00-\u9fa5]");
    Pattern rexCheruWord = Pattern.compile("切[切卟叮咧哔唎啪啰啵嘭噜噼巴拉蹦铃]+");

    /**
     * 本函数将单字翻译为切噜词
     * 
     * @param word 汉字
     * @return 切噜词
     */
    public String wordToCheru(String word) {
        StringBuilder res = new StringBuilder("切");
        byte[] tmp = word.getBytes(Charset.forName(ENCODING));

        for (byte b : tmp) {
            res.append(Character.toString(CHERU_SET.charAt(b & 0xf)));
            res.append(Character.toString(CHERU_SET.charAt(b >> 4 & 0xf)));
        }

        return res.toString();
    }

    /**
     * 
     * @param cheru 切噜词
     * @return 汉字
     * @throws UnsupportedEncodingException GB18030 编码失败会返回本异常
     */
    public String cheruToWord(String cheru) throws UnsupportedEncodingException {
        if (cheru.length() < 2 && cheru.charAt(0) != '切'){
            return cheru;
        }

        ArrayList<Byte> tmp = new ArrayList<Byte> ();
        String[] stringTemp = cheru.substring(1).split("");
        for (int i = 0; i < stringTemp.length - 1; i += 2) {
            byte t;
            t = (byte) CHERU_SET.indexOf(stringTemp[i + 1]);
            t = (byte) (t << 4 | (byte) CHERU_SET.indexOf(stringTemp[i]));
            tmp.add((Byte) t);
        }
        if (stringTemp.length % 2 == 1) {
            byte t = (byte) CHERU_SET.indexOf(stringTemp.length - 1);
            tmp.add((Byte) t);
        }

        byte[] res = new byte[tmp.size()];
        for (int i = 0; i < tmp.size(); i++) {
            res[i] = (byte) tmp.get(i);
        }
        
        return new String(res, ENCODING);
    }


    public String strToCheru(String s) {
        StringBuilder res = new StringBuilder();
        for (String x : rexSplit.split(s)) {
            if (rexWord.matcher(x).find()) {
                x = wordToCheru(x);
            }
            res.append(x);
        }
        return res.toString();
    }

    public String cheruToStr(String s) throws UnsupportedEncodingException {
        StringBuilder res = new StringBuilder();
        for (String x : rexSplit.split(s)) {
            if (rexCheruWord.matcher(x).matches()) {
                res.append(cheruToWord(x));
            }else {
                res.append(x);
            }
        }
        return res.toString();
    }

    @Override
    public int onGroupMessage(CoolQ cq, CQGroupMessageEvent event) {

        long groupId=event.getGroupId();
        String inMsg=event.getMessage();

        boolean cherulize=inMsg.matches("^切噜一下.*");
        boolean decherulize=inMsg.matches("^切噜～♪.*");        

        if (cherulize) {
            inMsg = inMsg.replaceFirst("切噜一下", "");
            if (inMsg.length() > 500) {
                cq.sendGroupMsg(groupId, "切、切噜太长切不动勒切噜噜...", false);
            }else {
                cq.sendGroupMsg(groupId, "'切噜～♪'" + strToCheru(inMsg), false);
            }
        }

        if (decherulize) {
            inMsg = inMsg.replaceFirst("切噜～♪", "");
            if (inMsg.length() > 1501) {
                cq.sendGroupMsg(groupId, "切、切噜太长切不动勒切噜噜...", false);
            }else {
                try {
                    cq.sendGroupMsg(groupId, "的切噜噜是：\\n" + cheruToStr(inMsg), false);
                } catch (UnsupportedEncodingException e) {
                    System.out.println("encoding exception");
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return super.onGroupMessage(cq, event);
    }
}