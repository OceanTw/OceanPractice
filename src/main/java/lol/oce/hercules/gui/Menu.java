package lol.oce.hercules.gui;

import lol.oce.hercules.gui.impl.DuelMenu;
import lol.oce.hercules.gui.impl.QueueMenu;
import lol.oce.hercules.gui.impl.QueueRankedMenu;
import lol.oce.hercules.gui.impl.QueueUnrankedMenu;
import lombok.Getter;

@Getter
public class Menu {
    QueueMenu queueMenu = new QueueMenu();
    QueueUnrankedMenu queueUnrankedMenu = new QueueUnrankedMenu();
    QueueRankedMenu queueRankedMenu = new QueueRankedMenu();
    DuelMenu duelMenu = new DuelMenu();
}
