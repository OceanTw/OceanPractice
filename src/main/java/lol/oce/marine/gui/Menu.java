package lol.oce.marine.gui;

import lol.oce.marine.gui.impl.DuelMenu;
import lol.oce.marine.gui.impl.QueueMenu;
import lol.oce.marine.gui.impl.QueueRankedMenu;
import lol.oce.marine.gui.impl.QueueUnrankedMenu;
import lombok.Getter;

@Getter
public class Menu {
    QueueMenu queueMenu = new QueueMenu();
    QueueUnrankedMenu queueUnrankedMenu = new QueueUnrankedMenu();
    QueueRankedMenu queueRankedMenu = new QueueRankedMenu();
    DuelMenu duelMenu = new DuelMenu();
}
