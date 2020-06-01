package cn.edu.cuit.fatcat;

/*
FatCat fatCat = FatCat.getPreparer()
                        .port(8080)
                        .address("0.0.0.0")
                        .warPack("/usr/?.war")
                        .getInstance();
fatCat.startUp();
 */
public interface FatCat extends FunctionalModule {
    public void startUp();
}
