# POE Companion

  POE Companion is a helper app for Path of Exile. With POE Companion you can check equipped gear and socketed skill
gems for chosen character.

  Character data retrieved from Path of Exile API so Path of Exile profile privacy setting must be public.
Previously viewed characters will be cashed and accessible in a offline mode.

You can try it out using my account name Nzaka
or any character from public ladder(characters will be visible only if profile is public)
https://www.pathofexile.com/forum/view-thread/71278

# Screenshots
<img src="Screenshots/title_poeproladder.png?raw=true" width="260"> <img src="Screenshots/import_account_poeproladder.png?raw=true" width="260"> <img src="Screenshots/character_selection_poeproladder.png?raw=true" width="260"> <img src="Screenshots/inventory_poeproladder.png?raw=true" width="260"> <img src="Screenshots/item_selection_poeproladder.png?raw=true" width="260"> <img src="Screenshots/skill_links_poeproladder.png?raw=true" width="260">

# Libraries used

MVP pattern used for UI layer where presenters using observer pattern with help of rxjava2 for reactive UI updates

* **room-persistence-library**
* **dagger2** 
* **retrofit2** 
* **rxjava2**
* **glide**
* **moshi**
