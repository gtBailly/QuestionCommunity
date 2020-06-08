package net.bewithu.questioncommunity.Service;

import net.bewithu.questioncommunity.model.Question;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Wei
 */
@Service
public class SearchService {
    private final String solr_url = "http://127.0.0.1:8983/solr/questioncommunity";
    private HttpSolrClient client = new HttpSolrClient.Builder(solr_url).build();
    // 其实查询结果中，就是id, question_title, question_content, _version 这四个Field
    private static final String QUESTION_TITLE_FIELD = "question_title";
    private static final String QUESTION_CONTENT_FIELD = "question_content";
    private static final String QUERY_FIELD = "keyvalue";

    /**
     * 来执行solr的查询命令
     *
     * @param keyWord 查询单词
     * @param hlpre   前缀
     * @param hlpost  后缀
     * @param offset  第几个开始
     * @param count   展示的数目
     * @return 返回的检索值
     * @throws IOException
     * @throws SolrServerException
     */
    public List<Question> search(String keyWord, String hlpre, String hlpost, int offset, int count)
            throws IOException, SolrServerException {
        // keyWord 就是要查询的关键字，构造之后相当于 q:keyWord
        SolrQuery query = new SolrQuery(keyWord);
        List<Question> reslist = new ArrayList<>();
        // 每页显示记录数
        query.setRows(count);
        // 从offset开始查
        query.setStart(offset);
        // 开启高亮组件
        query.setHighlight(true);
        // 高亮前缀标记，比如 "<span color='red'>"
        query.setHighlightSimplePre(hlpre);
        // 高亮后缀标记，比如 "</span>"
        query.setHighlightSimplePost(hlpost);
        /* 设定高亮显示的字段，用空格或逗号隔开的字段列表
           不设置的话我们在managed-schema.xml默认设置会有一个<copyField source="*" dest="_text_"/>
           这个默认会把之前设置的 question_title 和 question_content 都复制到 _text_ Field中，进行搜索
         */
        query.set("hl.fl", QUESTION_TITLE_FIELD + "," + QUESTION_CONTENT_FIELD);
        // 指定搜索Field
        query.set("df", QUERY_FIELD);
        QueryResponse response = client.query(query);
        for (Map.Entry<String, Map<String, List<String>>> entry : response.getHighlighting().entrySet()) {
            Question question = new Question();
            question.setId(Integer.parseInt(entry.getKey()));
            // 高亮的搜索结果有些是question_title里面有高亮，有些是question_content中有高亮
            if (entry.getValue().containsKey(QUESTION_TITLE_FIELD)) {
                List<String> titleList = entry.getValue().get(QUESTION_TITLE_FIELD);
                if (titleList.size() > 0) {
                    question.setTitle(titleList.get(0));
                }
            }
            if (entry.getValue().containsKey(QUESTION_CONTENT_FIELD)) {
                List<String> contentList = entry.getValue().get(QUESTION_CONTENT_FIELD);
                if (contentList.size() > 0) {
                    question.setContent(contentList.get(0));
                }
            }
            reslist.add(question);
        }
        return reslist;
    }

    /**
     * 实时更新数据库的索引
     *
     * @param id
     * @param content
     * @param
     * @return
     * @throws IOException
     * @throws SolrServerException
     */
    public boolean indexUpdate(int id, String content, String title) throws IOException, SolrServerException {
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("id", id);
        doc.setField("question_content", content);
        doc.setField("question_title", title);
        UpdateResponse response = client.add(doc, 1000);
        return response != null && response.getStatus() == 0;
    }
}
