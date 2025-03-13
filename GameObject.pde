abstract class GameObject {
    public GameObject(PVector position) {
        this.position = position;
        this.velocity = new PVector(0, 0);
    }

    public void update() {
        this.position.add(this.velocity);
    }

    public abstract void draw();

    public abstract PVector getPosition();
}