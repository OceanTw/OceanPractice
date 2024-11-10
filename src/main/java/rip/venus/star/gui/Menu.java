package rip.venus.star.gui;

import rip.venus.star.gui.impl.DuelMenu;
import rip.venus.star.gui.impl.QueueMenu;
import rip.venus.star.gui.impl.QueueRankedMenu;
import rip.venus.star.gui.impl.QueueUnrankedMenu;
import lombok.Getter;

@Getter
public class Menu {
    QueueMenu queueMenu = new QueueMenu();
    QueueUnrankedMenu queueUnrankedMenu = new QueueUnrankedMenu();
    QueueRankedMenu queueRankedMenu = new QueueRankedMenu();
    DuelMenu duelMenu = new DuelMenu();
}
