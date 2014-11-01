/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/9/9 22:30</create-date>
 *
 * <copyright file="CommonDictioanry.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package com.hankcs.hanlp.corpus.dictionary;

import com.hankcs.hanlp.collection.trie.bintrie.BinTrie;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;

import static com.hankcs.hanlp.utility.Predefine.logger;

/**
 * 可以调整大小的词典
 *
 * @author hankcs
 */
public abstract class SimpleDictionary<V>
{
    BinTrie<V> trie = new BinTrie<>();

    public boolean load(String path)
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            String line;
            while ((line = br.readLine()) != null)
            {
                Map.Entry<String, V> entry = onGenerateEntry(line);
                if (entry == null) continue;
                trie.put(entry.getKey(), entry.getValue());
            }
            br.close();
        }
        catch (Exception e)
        {
            logger.warning("读取" + path + "失败" + e);
            return false;
        }
        return true;
    }

    /**
     * 查询一个单词
     *
     * @param key
     * @return 单词对应的条目
     */
    public V get(String key)
    {
        return trie.get(key);
    }

    /**
     * 由参数构造一个词条
     *
     * @param line
     * @return
     */
    protected abstract Map.Entry<String, V> onGenerateEntry(String line);

    /**
     * 以我为主词典，合并一个副词典
     * @param other
     */
    public void combine(SimpleDictionary<V> other)
    {
        if (other.trie == null)
        {
            logger.warning("有个词典还没加载");
            return;
        }
        for (Map.Entry<String, V> entry : other.trie.entrySet())
        {
            if (trie.containsKey(entry.getKey())) continue;
            trie.put(entry.getKey(), entry.getValue());
        }
    }
    /**
     * 获取键值对集合
     * @return
     */
    public Set<Map.Entry<String, V>> entrySet()
    {
        return trie.entrySet();
    }

}
