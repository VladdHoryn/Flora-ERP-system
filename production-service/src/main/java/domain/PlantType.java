package domain;

public enum PlantType {

    GINKGO("ginkgo"),
    CEDARS("cedars"),
    CYPRESS_TREE("cypress tree"),
    METASEQUOIA("metasequoia"),
    LARCH("larch"),
    PSEUDOTSUGA("pseudotsuga"),
    PINE("pine"),
    THOUSAND("thousand"),
    THUJA("thuja"),
    JUNIPERS("junipers"),
    SPRUCE("spruce"),
    FIR("fir"),
    RASPBERRY_SEEDLINGS("raspberry seedlings"),
    CURRANT_SEEDLINGS("currant seedlings"),
    BIENNIAL_SEEDLINGS("biennial seedlings"),
    GARDEN_TREE("garden tree"),
    APRICOT_SEEDLINGS("apricot seedlings"),
    GOOSEBERRY_SEEDLINGS("gooseberry seedlings"),
    QUINCE_SEEDLINGS("quince seedlings"),
    CHERRY_SEEDLINGS("cherry seedlings"),
    PEAR_SEEDLINGS("pear seedlings"),
    SEEDLINGS_OF_COLUMNAR_TREES("seedlings of columnar trees"),
    NECTARINE_SEEDLINGS("nectarine seedlings"),
    BLACKBERRY_SEEDLINGS("blackberry seedlings"),
    PEACH_SEEDLINGS("peach seedlings"),
    PLUM_SEEDLINGS("plum seedlings"),
    SEEDLINGS_OF_CROSSED_VARIETIES("seedlings of crossed varieties"),
    MULBERRY_SEEDLINGS("mulberry seedlings"),
    APPLE_SEEDLINGS("apple seedlings");

    private final String value;

    PlantType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}