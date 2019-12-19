
public class PathChecker implements Runnable {
    private int[] path;
    private int[][] adjacency;

    PathChecker(int[] path, int[][] adjacency){
        this.path=path;
        this.adjacency=adjacency;
    }

    @Override
    public void run() {
        if(!AppStart.cv) {
            for (int i = 0; i < path.length - 1; i++) {
                if (!(adjacency[path[i]][path[i + 1]] == 1 && adjacency[path[i + 1]][path[i]] == 1)) {
                    return;
                }
            }
            if (!(adjacency[path[path.length-1]][path[0]] == 1 && adjacency[path[0]][path[path.length-1]] == 1)){
                return;
            }
            AppStart.cv=true;
            System.out.println("\nSolution found!");
            StringBuilder res = new StringBuilder();
            for (int value : path) {
                res.append(Integer.toString(value)).append(' ');
            }
            System.out.println(res);
        }
    }
}
