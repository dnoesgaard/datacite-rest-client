package org.gbif.datacite.rest.client.retrofit;

import com.github.jasminb.jsonapi.JSONAPIDocument;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.apache.commons.io.IOUtils;
import org.gbif.datacite.rest.client.configuration.ClientConfiguration;
import org.gbif.datacite.rest.client.model.DoiSimplifiedModel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.gbif.datacite.rest.client.retrofit.DataCiteRetrofitClientCommonSteps.DOI_PREFIX;
import static org.gbif.datacite.rest.client.retrofit.DataCiteRetrofitClientCommonSteps.currentDoi;
import static org.gbif.datacite.rest.client.retrofit.DataCiteRetrofitClientCommonSteps.model;
import static org.gbif.datacite.rest.client.retrofit.DataCiteRetrofitClientCommonSteps.response;

/**
 * Logic class for {@link DataCiteRetrofitClientIT} for negative cases.
 */
public class DataCiteRetrofitClientNegativeSteps {

    private static final ITPropertiesManager IT_PROPERTIES = ITPropertiesManager.getInstance();
    public static final String DOI_WITH_INCORRECT_PREFIX = "10.2222/111";

    private DataCiteRetrofitSyncClient wrongClient;

    @Given("^Misconfigured rest client: wrong \"([^\"]*)\"$")
    public void restClientWithWrongCredentials(String param) {
        ClientConfiguration clientConfiguration = ClientConfiguration
                .builder()
                .withBaseApiUrl(param.contains("api") ?
                        IT_PROPERTIES.get("datacite.api.base.url").replaceAll("https", "http") :
                        IT_PROPERTIES.get("datacite.api.base.url"))
                .withUser(param.contains("username") ? "wrong_user" : IT_PROPERTIES.get("datacite.user"))
                .withPassword(param.contains("password") ? "wrong_password" : IT_PROPERTIES.get("datacite.password"))
                .build();

        wrongClient = new DataCiteRetrofitSyncClient(clientConfiguration);
    }

    @Given("^Model$")
    public void model() {
        model = new DoiSimplifiedModel();
        model.setDoi("wrong_doi");
    }

    @Given("^Model with wrong \"([^\"]*)\"$")
    public void modelWithWrongUrl(String field) throws IOException {
        String doi = TestDoiProvider.get(DOI_PREFIX);
        model = new DoiSimplifiedModel();
        model.setDoi(doi);
        model.setId(doi);
        String xmlMetadata = IOUtils.toString(
                this.getClass().getResourceAsStream("/datacite-example-full-v4.xml"),
                StandardCharsets.UTF_8);
        switch (field) {
            case "URL":
                model.setUrl("wrong_URL");
                break;
            case "metadata":
                xmlMetadata = "<wrong_metadata/>";
                break;
            case "event":
                model.setEvent("wrong_event");
                break;
            case "doi":
                model.setId(DOI_WITH_INCORRECT_PREFIX);
                model.setDoi(DOI_WITH_INCORRECT_PREFIX);
                break;
            default:
        }
        model.setXml(Base64.getEncoder().encodeToString(xmlMetadata.getBytes(StandardCharsets.UTF_8)));
    }

    @Given("^Model with this DOI$")
    public void modelWithExistingDoi() {
        model = new DoiSimplifiedModel();
        model.setDoi(currentDoi);
        model.setId(currentDoi);
    }

    @When("^Perform a request with a misconfigured client to DataCite's POST DOI$")
    public void performPostRequestWithWrongCredentials() {
        response = wrongClient.createDoi(new JSONAPIDocument<>(model));
    }

}
