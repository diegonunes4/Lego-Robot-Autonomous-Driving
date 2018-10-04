import lejos.nxt.*;

public class mp {
	//declarar variaveis
	static boolean quit=false;
	static int start_time,lag_time;
	static int luzDir,luzEsq, distFrente, distLado ,estado, dist;
	static final int SPEED = 200;

	public static void main(String [] args) throws InterruptedException {

		LightSensor luz11 = new LightSensor(SensorPort.S1);
		LightSensor luz22 = new LightSensor(SensorPort.S4);
		UltrasonicSensor ultraSomFrente = new UltrasonicSensor(SensorPort.S2);
		UltrasonicSensor ultraSomLado = new UltrasonicSensor(SensorPort.S3);

		int flag2 = 0;
		estado = 0;
		Thread.sleep(1000); // espera 1 segundo

		while(!quit) {
			start_time=(int)System.currentTimeMillis();
			
			// Obter valores dos sensores
			luzDir = luz11.readValue();
			luzEsq = luz22.readValue();
			distFrente = ultraSomFrente.getDistance();
			distLado = ultraSomLado.getDistance();

			// Estado 0 : Para
			if(estado == 0){
				
				LCD.drawString("ESTOU NO ZERO", 0, 6);
				
				Motor.A.stop();
				Motor.A.setSpeed(0);
				Motor.C.stop();
				Motor.C.setSpeed(0);
				estado = 1;
			}

			// Estado 1 : Anda em frente
			if (estado ==1) {

				Motor.A.forward();
				Motor.A.setSpeed(SPEED+150);
				Motor.C.forward();
				Motor.C.setSpeed(SPEED+250);
				
				// Se detetar preto no sensor da direita
				if(luzDir < 35) {
					estado = 2;
				}

				// Se detetar preto no sensor da esquerda
				if(luzEsq < 35) {
					estado = 3;
				}
				
				// Se detetar preto em ambos os sensores
				if((luzDir < 35) && (luzEsq < 35)) {
					estado = 4;	
				}
				
				// Sensor da frente deteta a 20
				if(distFrente <= 20) {
					estado = 6;
				}
			}

			// Estado 2 : Vira um pouco à Esq.
			if (estado == 2) {
				Motor.A.forward();
				Motor.A.setSpeed(SPEED);
				Motor.C.forward();
				Motor.C.setSpeed(0);

				estado = 1;
			}

			// Estado 3 : Vira um pouco à Dir.
			if (estado == 3) {
				Motor.A.forward();
				Motor.A.setSpeed(0);
				Motor.C.forward();
				Motor.C.setSpeed(SPEED + 50);

				estado = 1;
			}

			// Estado 4 : Para 2s
			if (estado == 4) {

				//LCD.drawString("passadeira", 0, 6);
				Motor.C.stop();
				Motor.A.stop();
				Motor.A.setSpeed(0);
				Motor.C.setSpeed(0);
				
				Thread.sleep(3000);

				Motor.A.forward();
				Motor.C.forward();
				Motor.A.setSpeed(SPEED + 200);
				Motor.C.setSpeed(SPEED + 200);
				
				estado = 5;
			}
			
			/* Avança 3 segundos para não parar 
				na segunda linha da passadeira */
			if(estado == 5) {	
			
				Motor.A.forward();
				Motor.A.setSpeed(SPEED+148);
				Motor.C.forward();
				Motor.C.setSpeed(SPEED+100);

				Thread.sleep(2200);

				estado = 1;
			}

			// Estado 6 : Roda ~ 90º à esquerda, quando deteta obstáculo
			if(estado == 6) {	
				
				Motor.C.stop();
				Motor.A.stop();
				Motor.A.setSpeed(200);
				Motor.A.rotate(360);
				Motor.C.stop();
				
				estado = 7;
			}

			if(estado == 7) {
				distLado = ultraSomLado.getDistance();
				if(distLado < 30)
				{
					Motor.A.setSpeed(SPEED);
					Motor.C.setSpeed(SPEED);
					Motor.A.forward();
					Motor.C.forward();
				}
				
				if(distLado >= 30)
				{
					estado = 8;
				}
			}
			
			if(estado == 8) {
				distLado = ultraSomLado.getDistance();
				Motor.C.stop();
				Motor.A.stop();
				Motor.C.setSpeed(900);
				Motor.C.rotate(450);
				
				estado = 9;
			}
			
			if(estado == 9)
			{
				distLado = ultraSomLado.getDistance();
				Motor.A.setSpeed(SPEED+200);
				Motor.C.setSpeed(SPEED+200);
				Motor.A.forward();
				Motor.C.forward();
				
				if(distLado < 30)
				{
					estado = 10;
				}
			}
			
			if(estado == 10)
			{
				distLado = ultraSomLado.getDistance();
				
				Motor.A.forward();
				Motor.C.forward();
				Motor.A.setSpeed(SPEED);
				Motor.C.setSpeed(SPEED);
				
				if(distLado < 25)
				{
					estado = 11;
				}
				
				if(distLado >= 25 && distLado <= 50)
				{
					estado = 13;
				}
				
				if(distLado > 50)
				{
					//estado = 14;
					estado = 1000;
				}
			}
			
			if(estado == 11)
			{
				Motor.A.setSpeed(SPEED+5);
				Motor.C.setSpeed(SPEED);
				Motor.A.forward();
				Motor.C.forward();
				estado = 10;
			}
			
			if(estado == 13)
			{
				Motor.A.setSpeed(SPEED);
				Motor.C.setSpeed(SPEED+5);
				Motor.A.forward();
				Motor.C.forward();
				estado = 10;
			}
			
			if(estado == 1000)
				//Robo roda para a direita
			{
				Motor.A.stop();
				Motor.C.setSpeed(500);
				Motor.A.setSpeed(0);
				
				Thread.sleep(500);
				estado = 1001;
			}
			
			if(estado == 1001)
			{
				luzDir = luz11.readValue();
				luzEsq = luz22.readValue();
				
				Motor.A.forward();
				Motor.C.forward();
				Motor.C.setSpeed(SPEED);
				Motor.A.setSpeed(SPEED);
	
				if(luzDir <= 40)
				{
					Motor.C.stop();
					Motor.A.setSpeed(400);
					Motor.C.stop();
					
					Thread.sleep(500);
					estado = 1;
				}
			}
			
//			if(estado == 14)
//			{
//				
//				Motor.A.setSpeed(SPEED);
//				Motor.C.setSpeed(SPEED+10);
//				Motor.A.forward();
//				Motor.C.forward();
//				
//				if(luzDir < 35)
//				{
//					estado = 15;
//				}
//			}
			
//			
//			if(estado == 15)
//			{
//				Motor.A.setSpeed(SPEED+200);
//				Motor.C.setSpeed(0);
//				Motor.A.forward();
//				Motor.C.forward();
//				estado = 1;
//			}

			lag_time=(int)System.currentTimeMillis()-start_time;

			LCD.clear();
			LCD.drawString("lag=", 0, 0);
			LCD.drawInt((int)(lag_time), 4, 0);

			LCD.drawString("luzD=", 0, 2);
			LCD.drawInt((int)(luzDir), 5, 2);

			LCD.drawString("luzE=", 0, 3);
			LCD.drawInt((int)(luzEsq), 5, 3);			
			
			LCD.drawString("distLado", 0, 4);
			LCD.drawInt((int)(distLado), 6, 4);	

			if(estado == 0) {
				//LCD.drawString("parado(0)", 0, 6);
			}

			if(estado == 1) {
				LCD.drawString("anda em frente(1)", 0, 6);
			}

			if(estado == 4) {
				LCD.drawString("espera(4)", 0, 6);
			}

			if(estado == 5) {
				LCD.drawString("contorna(5)", 0, 6);
			}

			if(estado == 6) {
				LCD.drawString("estado: 6", 0, 6);
			}

			if(estado == 7) {
				LCD.drawString("estado: 7", 0, 6);
			}

			if(estado == 8) {
				LCD.drawString("estado: 8", 0, 6);
			}

			if(estado == 9) {
				LCD.drawString("estado: 9", 0, 6);
			}

			if(estado == 10) {
				LCD.drawString("estado: 10", 0, 6);
			}

			if(estado == 11) {
				LCD.drawString("estado: 11", 0, 6);
			}

			if(estado == 12) {
				LCD.drawString("estado: 12", 0, 6);
			}
			
			if(estado == 13) {
				LCD.drawString("estado: 13", 0, 6);
			}
			
			if(estado == 14) {
				LCD.drawString("estado: 14", 0, 6);
			}
			
			if(estado == 15) {
				LCD.drawString("estado: 15", 0, 6);
			}
			
			if(estado == 1000) {
				LCD.drawString("estado: 1000", 0, 6);
			}

			LCD.refresh();

			if(Button.RIGHT.isPressed()) {
				estado=1; // vai para o estado 1 (anda em frente) se carregar no botao RIGHT
			}

			if(Button.ESCAPE.isPressed()) {
				quit=true;
			}

			if ((50-lag_time)>0) {
				Thread.sleep(50-lag_time);
			}

		} // fim do while
	} // fim do metodo main
} // fim da classe exemplo