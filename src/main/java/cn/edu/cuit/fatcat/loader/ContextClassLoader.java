package cn.edu.cuit.fatcat.loader;

public class ContextClassLoader extends FatCatClassLoader {
    private static ContextClassLoader contextClassLoader;

    public static ContextClassLoader getInstance() {
        if (contextClassLoader == null) {
            synchronized (ContextClassLoader.class) {
                if (contextClassLoader == null) {
                    contextClassLoader = new ContextClassLoader();
                }
            }
        }
        return contextClassLoader;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}
