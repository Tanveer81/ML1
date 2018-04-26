public class featureVector {
    public feature f[] = new feature[21];

    public featureVector() {
        for (int i = 0; i < 21; i++) {
            f[i] = new feature();
        }
    }
}
