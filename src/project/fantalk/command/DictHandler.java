package project.fantalk.command;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import project.fantalk.xmpp.Message;
import project.fantalk.xmpp.XMPPUtils;

public class DictHandler extends BaseCommand {

    private String email;

    public DictHandler() {
        super("query", "c", "cha");
    }

    @Override
    public void doCommand(Message message, String argument) {
        String word = argument;
        this.email = message.email;
        doSearch(word);
    }

    public String documentation() {
        return "-cha/-c someword 查询单词(中英皆可)的含义";
    }

    private void doSearch(String word) {
        try {
            URL url = new URL("http://dict.cn/ws.php?q="
                    + URLEncoder.encode(word, "GBK"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            XMLDataHandler handler = new XMLDataHandler(word);
            parser.parse(is, handler);
            DictData data = handler.getDictInfo();
            StringBuilder sb = new StringBuilder();
            if (data.invalid) {
                sb.append("404 Not Found.");
            } else {
                sb.append(data.key).append(": ").append(data.def);
            }
            XMPPUtils.sendMessage(sb.toString().replaceAll("\n", " "), email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class XMLDataHandler extends DefaultHandler {
        private DictData data;
        private boolean inDict = false;
        private boolean isKey = false;
        private boolean isDef = false;

        public DictData getDictInfo() {
            return data;
        }

        public XMLDataHandler(String word) {
            data = new DictData(word);
        }

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            if (isKey) {
                data.key = new String(ch, start, length);
            } else if (isDef) {
                data.def = new String(ch, start, length);
            }
        }

        @Override
        public void endElement(String uri, String localName, String name)
                throws SAXException {
            String tagName = localName.length() != 0 ? localName : name;
            tagName = tagName.toLowerCase().trim();
            if (tagName.equals("dict")) {
                inDict = false;
            }
            if (inDict) {
                if (tagName.equals("key")) {
                    isKey = false;
                } else if (tagName.equals("def")) {
                    isDef = false;
                }
            }
        }

        @Override
        public void startElement(String uri, String localName, String name,
                Attributes attributes) throws SAXException {
            String tagName = localName.length() != 0 ? localName : name;
            tagName = tagName.toLowerCase().trim();
            if (tagName.equals("dict")) {
                inDict = true;
            }
            if (inDict) {
                if (tagName.equals("key")) {
                    isKey = true;
                    data.invalid = false;
                } else if (tagName.equals("def")) {
                    isDef = true;
                }
            }
        }

    }

    private class DictData {
        public boolean invalid;
        public String key;// 查询的词
        public String def;//
        public String word;

        public DictData(String word) {
            this.word = word;
            this.invalid = true;
            this.key = "";
            this.def = "";
        }
    }

}
