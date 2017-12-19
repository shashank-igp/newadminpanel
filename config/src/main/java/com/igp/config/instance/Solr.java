package com.igp.config.instance;

import com.igp.config.SolrProperties;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Solr {

    private static final Logger logger = LoggerFactory.getLogger(Solr.class);

    private String zookeeper = "";
    private static CloudSolrClient cloudSolrClientForCard = null;
    private static CloudSolrClient cloudSolrClientForProduct = null;
    private final Object lock = new Object();
    private static Solr solr = null;
    private String productSolr = "";
    private String cardSolr = "";
    private static SolrClient solrClientProduct = null;
    private static SolrClient solrClientCard = null;

    private Solr() throws Exception {
        getConnectionForCard();
        getConnectionForProduct();
    }

    public SolrClient getConnectionForCard() throws SolrException {
        try {
            synchronized (lock) {
                if (cardSolr == null || cardSolr.equals("") || cardSolr.length() == 0) {
                    cardSolr = SolrProperties.getSolrCardInstance();
                }
                if (solrClientCard == null) {
                    solrClientCard = new HttpSolrClient.Builder(cardSolr).build();
                }
            }
        } catch (SolrException solrException) {
            logger.error("Solr failed to get a connection: ", solrException);
            throw solrException;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return solrClientCard;
    }

    public SolrClient getConnectionForProduct() throws SolrException {
        try {
            synchronized (lock) {
                if (productSolr == null || productSolr.equals("") || productSolr.length() == 0) {
                    productSolr = SolrProperties.getSolrProductInstance();
                }
                if (solrClientProduct == null) {
                    solrClientProduct = new HttpSolrClient.Builder(productSolr).build();
                }
            }
        } catch (SolrException solrException) {
            logger.error("Solr failed to get a connection: ", solrException);
            throw solrException;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return solrClientProduct;
    }

    public synchronized static Solr getInstance() throws Exception {
        try {
            if (solr == null) {
                solr = new Solr();
            }
            return solr;
        } catch (Exception e) {
            throw e;
        }
    }
}
