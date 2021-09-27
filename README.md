
## Dialogs

A module that user interfaces for in-game dialogs.

![image1](images/2015-10-14_screenshot.png "Light and Shadow Team Selection Dialog")


### How to use the `Dialog` Component

Like every other component, the `Dialog` component needs to be placed in a [Prefab](https://github.com/Terasology/TutorialAssetSystem/wiki/Prefab-based-Configuration). It needs to specify a `firstPage` and at least one `page` with one `response`:

```json5
    "Dialog": {
        "firstPage": "main",
        "pages": [
            {
                "id": "main",
                "title": "Welcome",
                "paragraphText": [
                    "This is the first part of this conversation."
                ],
                "responses": [
                    {
                        "text": "Very well..",
                        "responseImage": "engine:bubble",
                        "action": {
                            "class": "Dialogs:CloseDialogAction"
                        }
                    }
                ]
            }
        ]
    }
```

A `page` requires an `id` with which it can be identified, for instance to be configured as the first page or to be referenced in a response that links to it.
Pages further should have a `title`, a `paragraphText`, and `responses`.

Responses are `PlayerAction`s that the player can initiate to continue the conversation, end it or start a new one. Which action a response will trigger depends on its `action` property. The following options are currently present:
- `ChangeDialogAction`
- `CloseDialogAction`
- `NewDialogAction`
Please note, that additional actions might've been added to https://github.com/Terasology/Dialogs/tree/develop/src/main/java/org/terasology/dialogs/action.

The player cannot see in-game which action a response will trigger, so please make sure to formulate the response's `text` clearly and leverage indicative icons as the response's `responseImage`, for instance a ❎ to indicate a response will end the conversation.


### License

This module is licensed under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).
