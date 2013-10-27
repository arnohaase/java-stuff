package de.arnohaase.javastuff._40_callbackfake;

import java.awt.Dimension;

import javax.swing.JButton;


class EnlargedButton extends JButton {
	@Override
	public void setSize(Dimension d) {
		super.setSize(new Dimension (d.width + 10, d.height + 10));
	}
	
	@Override
	public void resize(Dimension d) {
//		super.resize(new Dimension (d.width + 10, d.height + 10));
		setSize(d); // stack overflow
	}
}


public class FakeCallback extends JButton {
	public static void main(String[] args) {
		final JButton button = new EnlargedButton();
		button.setSize(new Dimension (100, 100));
		
		System.out.println(button.getSize());
	}
}