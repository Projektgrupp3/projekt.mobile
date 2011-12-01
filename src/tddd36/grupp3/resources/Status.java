package tddd36.grupp3.resources;

public enum Status {
	RECIEVED(){
		public String toString(){
			return "RECIEVED";
		}
	},
	THERE(){
		public String toString(){
			return "THERE";
		}
	},
	LOADED(){
		public String toString(){
			return "LOADED";
		}
	},
	DEPART(){
		public String toString(){
			return "DEPART";
		}
	},
	HOME(){
		public String toString(){
			return "HOME";
		}
	};
}
