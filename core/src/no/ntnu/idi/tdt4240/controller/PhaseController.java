package no.ntnu.idi.tdt4240.controller;

import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.model.FortifyModel;
import no.ntnu.idi.tdt4240.model.PhaseModel;
import no.ntnu.idi.tdt4240.view.PhaseView;

public class PhaseController {
    private PhaseModel model;
    private PhaseView view;
    public PhaseController(PhaseView view, PhaseModel model){
        this.model = model;
        this.view = view;
    }

    public void boardClicked(Vector2 touchWorldPos){ // maybe make interface for this

    }

    public void territoryClicked(Territory territory){ // called GameController
        model.getPhase().territoryClicked(territory); //update the model
        // update the view
        switch(model.getPhase().getName()){
            case "Place":
                break;
            case "Attack":
                break;
            case "Fortify":{
                PhaseModel.FortifyPhase phase = (PhaseModel.FortifyPhase)model.getPhase();
                //phase.getSelectedFrom();
                //update UI
                System.out.println("Draw line");
                view.onSelectedTerritoriesChange(phase.getSelectedFrom(), phase.getSelectedTo());
                break;
            }
            default:
                break;
        }
    }
}
