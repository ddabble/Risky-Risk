package no.ntnu.idi.tdt4240.controller;

import no.ntnu.idi.tdt4240.model.PhaseModel;
import no.ntnu.idi.tdt4240.view.PhaseView;

public class PhaseController {
    PhaseModel model;
    PhaseViewer viewer;

    public PhaseController(PhaseViewer viewer) {
        //A controller needs a view to control, and manages the model
        this.viewer = viewer;
        this.model = new PhaseModel();
        this.updatePhase();
    }

    public void nextPhaseButtonClicked() {
        model.nextPhase();
        this.updatePhase();
    }

    public void updatePhase() {
        String curPhase = this.model.getPhase().getName();
        String nextPhase = this.model.getPhase().next().getName();
        viewer.updatePhase(curPhase, nextPhase);
    }

}
