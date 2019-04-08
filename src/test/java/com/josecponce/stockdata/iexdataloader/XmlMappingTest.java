package com.josecponce.stockdata.iexdataloader;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.josecponce.stockdata.iexdataloader.gov.dataapi.TreasuryDataClient;
import com.josecponce.stockdata.iexdataloader.gov.dataapi.TreasuryYieldFeed;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class XmlMappingTest {
    @Autowired
    private TreasuryDataClient client;

    @Test
    public void test() throws IOException {
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        val mapper = new XmlMapper(module);
        val feed = mapper.readerFor(TreasuryYieldFeed.class).<TreasuryYieldFeed>readValue(xml);
        Assert.assertTrue(feed.getEntries().size() > 1);
    }

    @Test
    public void apiTest() throws IOException {
        val result = client.requestLastNDays(10);
        Assert.assertNotNull(result);
    }

    private final String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>\n" +
            "<feed xml:base=\"http://data.treasury.gov/Feed.svc/\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns=\"http://www.w3.org/2005/Atom\">\n" +
            "  <title type=\"text\">DailyTreasuryYieldCurveRateData</title>\n" +
            "  <id>http://data.treasury.gov/feed.svc/DailyTreasuryYieldCurveRateData</id>\n" +
            "  <updated>2019-03-24T18:20:01Z</updated>\n" +
            "  <link rel=\"self\" title=\"DailyTreasuryYieldCurveRateData\" href=\"DailyTreasuryYieldCurveRateData\" />\n" +
            "  <entry>\n" +
            "    <id>http://data.treasury.gov/Feed.svc/DailyTreasuryYieldCurveRateData(7298)</id>\n" +
            "    <title type=\"text\"></title>\n" +
            "    <updated>2019-03-24T18:20:01Z</updated>\n" +
            "    <author>\n" +
            "      <name />\n" +
            "    </author>\n" +
            "    <link rel=\"edit\" title=\"DailyTreasuryYieldCurveRateDatum\" href=\"DailyTreasuryYieldCurveRateData(7298)\" />\n" +
            "    <category term=\"TreasuryDataWarehouseModel.DailyTreasuryYieldCurveRateDatum\" scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\" />\n" +
            "    <content type=\"application/xml\">\n" +
            "      <m:properties>\n" +
            "        <d:Id m:type=\"Edm.Int32\">7298</d:Id>\n" +
            "        <d:NEW_DATE m:type=\"Edm.DateTime\">2019-03-01T00:00:00</d:NEW_DATE>\n" +
            "        <d:BC_1MONTH m:type=\"Edm.Double\">2.44</d:BC_1MONTH>\n" +
            "        <d:BC_2MONTH m:type=\"Edm.Double\">2.46</d:BC_2MONTH>\n" +
            "        <d:BC_3MONTH m:type=\"Edm.Double\">2.44</d:BC_3MONTH>\n" +
            "        <d:BC_6MONTH m:type=\"Edm.Double\">2.52</d:BC_6MONTH>\n" +
            "        <d:BC_1YEAR m:type=\"Edm.Double\">2.55</d:BC_1YEAR>\n" +
            "        <d:BC_2YEAR m:type=\"Edm.Double\">2.55</d:BC_2YEAR>\n" +
            "        <d:BC_3YEAR m:type=\"Edm.Double\">2.54</d:BC_3YEAR>\n" +
            "        <d:BC_5YEAR m:type=\"Edm.Double\">2.56</d:BC_5YEAR>\n" +
            "        <d:BC_7YEAR m:type=\"Edm.Double\">2.67</d:BC_7YEAR>\n" +
            "        <d:BC_10YEAR m:type=\"Edm.Double\">2.76</d:BC_10YEAR>\n" +
            "        <d:BC_20YEAR m:type=\"Edm.Double\">2.97</d:BC_20YEAR>\n" +
            "        <d:BC_30YEAR m:type=\"Edm.Double\">3.13</d:BC_30YEAR>\n" +
            "        <d:BC_30YEARDISPLAY m:type=\"Edm.Double\">3.13</d:BC_30YEARDISPLAY>\n" +
            "      </m:properties>\n" +
            "    </content>\n" +
            "  </entry>\n" +
            "  <entry>\n" +
            "    <id>http://data.treasury.gov/Feed.svc/DailyTreasuryYieldCurveRateData(7299)</id>\n" +
            "    <title type=\"text\"></title>\n" +
            "    <updated>2019-03-24T18:20:01Z</updated>\n" +
            "    <author>\n" +
            "      <name />\n" +
            "    </author>\n" +
            "    <link rel=\"edit\" title=\"DailyTreasuryYieldCurveRateDatum\" href=\"DailyTreasuryYieldCurveRateData(7299)\" />\n" +
            "    <category term=\"TreasuryDataWarehouseModel.DailyTreasuryYieldCurveRateDatum\" scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\" />\n" +
            "    <content type=\"application/xml\">\n" +
            "      <m:properties>\n" +
            "        <d:Id m:type=\"Edm.Int32\">7299</d:Id>\n" +
            "        <d:NEW_DATE m:type=\"Edm.DateTime\">2019-03-04T00:00:00</d:NEW_DATE>\n" +
            "        <d:BC_1MONTH m:type=\"Edm.Double\">2.45</d:BC_1MONTH>\n" +
            "        <d:BC_2MONTH m:type=\"Edm.Double\">2.46</d:BC_2MONTH>\n" +
            "        <d:BC_3MONTH m:type=\"Edm.Double\">2.46</d:BC_3MONTH>\n" +
            "        <d:BC_6MONTH m:type=\"Edm.Double\">2.54</d:BC_6MONTH>\n" +
            "        <d:BC_1YEAR m:type=\"Edm.Double\">2.54</d:BC_1YEAR>\n" +
            "        <d:BC_2YEAR m:type=\"Edm.Double\">2.55</d:BC_2YEAR>\n" +
            "        <d:BC_3YEAR m:type=\"Edm.Double\">2.52</d:BC_3YEAR>\n" +
            "        <d:BC_5YEAR m:type=\"Edm.Double\">2.53</d:BC_5YEAR>\n" +
            "        <d:BC_7YEAR m:type=\"Edm.Double\">2.63</d:BC_7YEAR>\n" +
            "        <d:BC_10YEAR m:type=\"Edm.Double\">2.72</d:BC_10YEAR>\n" +
            "        <d:BC_20YEAR m:type=\"Edm.Double\">2.93</d:BC_20YEAR>\n" +
            "        <d:BC_30YEAR m:type=\"Edm.Double\">3.09</d:BC_30YEAR>\n" +
            "        <d:BC_30YEARDISPLAY m:type=\"Edm.Double\">3.09</d:BC_30YEARDISPLAY>\n" +
            "      </m:properties>\n" +
            "    </content>\n" +
            "  </entry>\n" +
            "</feed>";
}
