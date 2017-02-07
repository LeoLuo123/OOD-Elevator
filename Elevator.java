class Elevator{
	private int id, currFloor;
	private final static MAXFLOOR = 7;
	private final static MAXCAP = 15;
	private final static MAXWEIGHT = 1000;
	private enum State = {UP, DOWN, HOLD, ERROR};
	private State currState;
	private PriorityQueue<Integer> upQueue;
	private PriorityQueue<Integer> downQueue;
	public Elevator(int id){
		this.id = id;
		upQueue = new PriorityQueue<>();
		downQueue = new PriorityQueue<>();
		currState = State.HOLD;
		currFloor = 0;
	}
	public void goUp(){
		currFloor++;
	}
	public void goDown(){
		currFloor--;
	}
	public void processRequest(int desireLevel) throws InvalidInputException{
		if(desireLevel < 0 || desireLevel > MAXFLOOR)
			throw new InvalidInputException();
		if(desireLevel < currFloor){
				downQueue.add(desireLevel);
		}
		else if desireLevel > currFloor)
				upQueue.add(desireFloor);
		else{
			doorOpen();
		}
	}
}
class ControlManager{
	private final static NUMBEROFELEVATOR = 3;
	private enum State = {UP, DOWN, HOLD, ERROR};
	private Elevator[] elevatorList;
	private static ControlManager CMInstance = null;
	private ControlManager(){
		elevatorList = new Elevator[NUMBEROFELEVATOR];
		for(int i = 0;i < NUMBEROFELEVATOR;i++)
			elevatorList[i] = new Elevator(i);
	}
	public ControlManager getInstance(){
		if(CMInstance == null){
			synchronized(ControlManager.class){
				CMInstance = new ControlManager();
			}
		}
		return CMInstance;
	}
	public void addRequest(int orig, int dest){
		State direction = orig > dest? State.DOWN:State.UP;
		int result = Integer.MAX_VALUE, targetElevator = -1;
		for(int i = 0;i < NUMBEROFELEVATOR;i++){
			if(elevatorList[i].currState == ERROR)
				continue;
			if(elevatorList[i].currState == direction || elevatorList[i].currState == State.HOLD){
				if(result > Math.abs(dest - elevatorList[i].currFloor){
					result = dest - elevatorList[i].currFloor;
					targetElevator = i;
				}
			}
			else{
				PriorityQueue pq = elevatorList[i].currState == State.UP? upQueue:downQueue;
				if(result > Math.abs(dest - elevatorList[i].currFloor) + Math.abs(pq.getLast()+elevatorList[i].currFloor)) {
					result = Math.abs(dest - elevatorList[i].currFloor) + Math.abs(pq.getLast()+elevatorList[i].currFloor);
					targetElevator = i;
				}
			}
		}
		if(targetElevator != -1)
			elevatorList[targetElevator].addRequest(dest);
		else throw new NoValidElevatorException();
	}
}

class UserPanel{
	private ControlManager CM;
	private int currfloor;
	public UserPanel(int floor){
		CM = ControlManager.getInstance();
		currfloor = floor;
	}
	public void sendRequest(int dest){
		CM.addRequest(currfloor, dest);
	}
}