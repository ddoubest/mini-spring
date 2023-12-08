package com.minis.batis;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.jdbc.core.JdbcTemplate;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DefaultSqlSessionFactory implements SqlSessionFactory {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final HashMap<String, MapperNode> mapperNodes = new HashMap<>();
    private String mapperLocation;

    @Override
    public SqlSession openSession() {
        SqlSession sqlSession = new DefaultSqlSession();
        sqlSession.setJdbcTemplate(jdbcTemplate);
        sqlSession.setSqlSessionFactory(this);
        return sqlSession;
    }

    @Override
    public MapperNode getMapperNode(String name) {
        return mapperNodes.get(name);
    }

    private void init() {
        scanLocation(this.mapperLocation);
    }

    private void scanLocation(String mapperLocation) {
        String path = Objects.requireNonNull(this.getClass().getClassLoader().getResource(mapperLocation)).getPath();
        File file = new File(path);
        for (File subFile : Objects.requireNonNull(file.listFiles())) {
            if (subFile.isDirectory()) {
                scanLocation(mapperLocation + "/" + subFile.getName());
            } else {
                try {
                    buildMapperNodes(mapperLocation + "/" + subFile.getName());
                } catch (DocumentException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    @SuppressWarnings("unchecked")
    private void buildMapperNodes(String filePath) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        URL resource = this.getClass().getClassLoader().getResource(filePath);
        Document document = saxReader.read(resource);
        Element rootElement = document.getRootElement();
        String namespace = rootElement.attributeValue("namespace");

        for (Element node : (List<Element>) rootElement.elements()) {
            String id = node.attributeValue("id");
            String parameterType = node.attributeValue("parameterType");
            String resultType = node.attributeValue("resultType");
            String sql = node.getText();
            MapperNode mapperNode = new MapperNode();
            mapperNode.setNamespace(namespace);
            mapperNode.setId(id);
            mapperNode.setParameterType(parameterType);
            mapperNode.setResultType(resultType);
            mapperNode.setSql(sql);
            mapperNode.setParameter("");
            this.mapperNodes.put(namespace + "." + id, mapperNode);
        }
    }

    public String getMapperLocation() {
        return mapperLocation;
    }

    public void setMapperLocation(String mapperLocation) {
        this.mapperLocation = mapperLocation;
    }
}
