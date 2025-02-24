package org.turter.patrocl.data_mock.utils

import org.turter.patrocl.domain.model.menu.CategoriesTreeData
import org.turter.patrocl.domain.model.menu.CategoryInfo
import org.turter.patrocl.domain.model.menu.ModifierGroupInfo
import org.turter.patrocl.domain.model.menu.ModifierSchemeInfo
import org.turter.patrocl.domain.model.menu.ModifiersGroupsTreeData
import org.turter.patrocl.domain.model.menu.StationDishInfo
import org.turter.patrocl.domain.model.menu.StationModifierInfo
import org.turter.patrocl.domain.model.menu.deprecated.Category
import org.turter.patrocl.domain.model.menu.deprecated.Dish
import org.turter.patrocl.domain.model.menu.deprecated.DishModifier
import org.turter.patrocl.domain.model.menu.deprecated.ModifiersGroup

object MenuDataSupplier {

    fun getDishList() = listOf(
        StationDishInfo(
            id = "rk-dish-id-1",
            rkId = "rk-dish-id-1",
            guid = "dish-guid-1",
            code = "dish-code-1",
            name = "Цезарь с курицей",
            status = "ok",
            mainParentIdent = "category-id-1",
            kurs = "",
            qntDecDigits = 0,
            modiScheme = "",
            comboScheme = "",
            categPath = "Пивная кружка ROOT/Салаты",
            price = 45000
        ),
        StationDishInfo(
            id = "rk-dish-id-2",
            rkId = "rk-dish-id-2",
            guid = "dish-guid-2",
            code = "dish-code-2",
            name = "Салат с куриной печенью",
            status = "ok",
            mainParentIdent = "category-id-1",
            kurs = "",
            qntDecDigits = 0,
            modiScheme = "",
            comboScheme = "",
            categPath = "Пивная кружка ROOT/Салаты",
            price = 42000
        ),
        StationDishInfo(
            id = "rk-dish-id-3",
            rkId = "rk-dish-id-3",
            guid = "dish-guid-3",
            code = "dish-code-3",
            name = "Буритто",
            status = "ok",
            mainParentIdent = "category-id-2",
            kurs = "",
            qntDecDigits = 0,
            modiScheme = "",
            comboScheme = "",
            categPath = "Пивная кружка ROOT/Основные блюда/Горячее",
            price = 50000
        ),
        StationDishInfo(
            id = "rk-dish-id-4",
            rkId = "rk-dish-id-4",
            guid = "dish-guid-4",
            code = "dish-code-4",
            name = "Куриная отбивная",
            status = "ok",
            mainParentIdent = "category-id-2",
            kurs = "",
            qntDecDigits = 0,
            modiScheme = "",
            comboScheme = "",
            categPath = "Пивная кружка ROOT/Основные блюда/Горячее",
            price = 55000
        ),
        StationDishInfo(
            id = "rk-dish-id-5",
            rkId = "rk-dish-id-5",
            guid = "dish-guid-5",
            code = "dish-code-5",
            name = "Греча",
            status = "ok",
            mainParentIdent = "category-id-3",
            kurs = "",
            qntDecDigits = 0,
            modiScheme = "",
            comboScheme = "",
            categPath = "Пивная кружка ROOT/Основные блюда/Гарниры",
            price = 30000
        ),
        StationDishInfo(
            id = "rk-dish-id-6",
            rkId = "rk-dish-id-6",
            guid = "dish-guid-6",
            code = "dish-code-6",
            name = "Рис",
            status = "ok",
            mainParentIdent = "category-id-3",
            kurs = "",
            qntDecDigits = 0,
            modiScheme = "",
            comboScheme = "",
            categPath = "Пивная кружка ROOT/Основные блюда/Гарниры",
            price = 28000
        ),
        StationDishInfo(
            id = "rk-dish-id-7",
            rkId = "rk-dish-id-7",
            guid = "dish-guid-7",
            code = "dish-code-7",
            name = "Картофель айдахо",
            status = "ok",
            mainParentIdent = "category-id-3",
            kurs = "",
            qntDecDigits = 0,
            modiScheme = "",
            comboScheme = "",
            categPath = "Пивная кружка ROOT/Основные блюда/Гарниры",
            price = 32000
        ),
        StationDishInfo(
            id = "rk-dish-id-8",
            rkId = "rk-dish-id-8",
            guid = "dish-guid-8",
            code = "dish-code-8",
            name = "Филе миньон",
            status = "ok",
            mainParentIdent = "category-id-2",
            kurs = "",
            qntDecDigits = 0,
            modiScheme = "rk-modifier-scheme-id-1",
            comboScheme = "",
            categPath = "Пивная кружка ROOT/Основные блюда/Горячее",
            price = 150000
        ),
        StationDishInfo(
            id = "rk-dish-id-9",
            rkId = "rk-dish-id-9",
            guid = "dish-guid-9",
            code = "dish-code-9",
            name = "Рибай",
            status = "ok",
            mainParentIdent = "category-id-2",
            kurs = "",
            qntDecDigits = 0,
            modiScheme = "rk-modifier-scheme-id-1",
            comboScheme = "",
            categPath = "Пивная кружка ROOT/Основные блюда/Горячее",
            price = 170000
        )
    )

    fun getCategoryTree() = CategoriesTreeData(
        rootCategoryRkId = "rk-category-id-0",
        categories = listOf(
            CategoryInfo(
                id = "category-id-0",
                rkId = "rk-category-id-0",
                guid = "category-guid-0",
                code = "category-code-0",
                name = "Пивная кружка ROOT",
                status = "ok",
                mainParentIdent = "category-id-x",
                childIds = listOf("rk-category-id-1", "rk-category-id-01"),
                dishIds = listOf()
            ),
            CategoryInfo(
                id = "category-id-1",
                rkId = "rk-category-id-1",
                guid = "category-guid-1",
                code = "category-code-1",
                name = "Салаты",
                status = "ok",
                mainParentIdent = "rk-category-id-0",
                childIds = listOf(),
                dishIds = listOf("rk-dish-id-1", "rk-dish-id-2")
            ),
            CategoryInfo(
                id = "category-id-01",
                rkId = "rk-category-id-01",
                guid = "category-guid-01",
                code = "category-code-01",
                name = "Основные блюда",
                status = "ok",
                mainParentIdent = "rk-category-id-0",
                childIds = listOf("rk-category-id-2", "rk-category-id-3"),
                dishIds = listOf()
            ),
            CategoryInfo(
                id = "category-id-2",
                rkId = "rk-category-id-2",
                guid = "category-guid-2",
                code = "category-code-2",
                name = "Горячее",
                status = "ok",
                mainParentIdent = "rk-category-id-01",
                childIds = listOf(),
                dishIds = listOf("rk-dish-id-3", "rk-dish-id-4", "rk-dish-id-8", "rk-dish-id-9")
            ),
            CategoryInfo(
                id = "category-id-3",
                rkId = "rk-category-id-3",
                guid = "category-guid-3",
                code = "category-code-3",
                name = "Гарниры",
                status = "ok",
                mainParentIdent = "rk-category-id-01",
                childIds = listOf(),
                dishIds = listOf("rk-dish-id-5", "rk-dish-id-6", "rk-dish-id-7")
            )
        )
    )

    fun getModifierList() = listOf(
        StationModifierInfo(
            id = "modifier-id-1",
            rkId = "rk-modifier-id-1",
            guid = "modifier-guid-1",
            code = "modifier-code-1",
            name = "В ОДНУ ТАРЕЛКУ",
            status = "ok",
            mainParentIdent = "rk-modifiers-group-id-1",
            maxOneDish = 1,
            useLimitedQnt = true,
            price = 0
        ),
        StationModifierInfo(
            id = "modifier-id-2",
            rkId = "rk-modifier-id-2",
            guid = "modifier-guid-2",
            code = "modifier-code-2",
            name = "ЗАМЕНА",
            status = "ok",
            mainParentIdent = "rk-modifiers-group-id-1",
            maxOneDish = 1,
            useLimitedQnt = true,
            price = 0
        ),
        StationModifierInfo(
            id = "modifier-id-3",
            rkId = "rk-modifier-id-3",
            guid = "modifier-guid-3",
            code = "modifier-code-3",
            name = "НЕ ГОТОВИТЬ",
            status = "ok",
            mainParentIdent = "rk-modifiers-group-id-2",
            maxOneDish = 1,
            useLimitedQnt = true,
            price = 0
        ),
        StationModifierInfo(
            id = "modifier-id-rare",
            rkId = "rk-modifier-id-rare",
            guid = "modifier-guid-rare",
            code = "modifier-code-rare",
            name = "Rare",
            status = "ok",
            mainParentIdent = "rk-modifiers-group-id-3",
            maxOneDish = 1,
            useLimitedQnt = true,
            price = 0
        ),
        StationModifierInfo(
            id = "modifier-id-medium",
            rkId = "rk-modifier-id-medium",
            guid = "modifier-guid-medium",
            code = "modifier-code-medium",
            name = "Medium",
            status = "ok",
            mainParentIdent = "rk-modifiers-group-id-3",
            maxOneDish = 1,
            useLimitedQnt = true,
            price = 0
        ),
        StationModifierInfo(
            id = "modifier-id-well-done",
            rkId = "rk-modifier-id-well-done",
            guid = "modifier-guid-well-done",
            code = "modifier-code-well-done",
            name = "Well Done",
            status = "ok",
            mainParentIdent = "rk-modifiers-group-id-3",
            maxOneDish = 1,
            useLimitedQnt = true,
            price = 0
        )
    )

    fun getModifiersGroupTree() = ModifiersGroupsTreeData(
        rootGroupRkId = "rk-modifiers-group-id-0",
        groups = listOf(
            ModifierGroupInfo(
                id = "modifiers-group-id-0",
                rkId = "rk-modifiers-group-id-0",
                guid = "modifiers-group-guid-0",
                code = "modifiers-group-code-0",
                name = "Пивная кружка ROOT",
                status = "ok",
                mainParentIdent = "modifiers-group-id-x",
                childIds = listOf("rk-modifiers-group-id-1", "rk-modifiers-group-id-2"),
                modifierIds = listOf()
            ),
            ModifierGroupInfo(
                id = "modifiers-group-id-1",
                rkId = "rk-modifiers-group-id-1",
                guid = "modifiers-group-guid-1",
                code = "modifiers-group-code-1",
                name = "Зал",
                status = "ok",
                mainParentIdent = "rk-modifiers-group-id-0",
                childIds = listOf(),
                modifierIds = listOf("rk-modifier-id-1", "rk-modifier-id-2")
            ),
            ModifierGroupInfo(
                id = "modifiers-group-id-2",
                rkId = "rk-modifiers-group-id-2",
                guid = "modifiers-group-guid-2",
                code = "modifiers-group-code-2",
                name = "ОБЩЕЕ",
                status = "ok",
                mainParentIdent = "rk-modifiers-group-id-0",
                childIds = listOf(),
                modifierIds = listOf("rk-modifier-id-3", "rk-modifier-id-4", "rk-modifier-id-5")
            ),
            ModifierGroupInfo(
                id = "modifiers-group-id-3",
                rkId = "rk-modifiers-group-id-3",
                guid = "modifiers-group-guid-3",
                code = "modifiers-group-code-3",
                name = "Для стейков",
                status = "ok",
                mainParentIdent = "rk-modifiers-group-id-x",
                childIds = listOf(),
                modifierIds = listOf(
                    "rk-modifier-id-rare",
                    "rk-modifier-id-medium",
                    "rk-modifier-id-well-done"
                )
            )
        )
    )

    fun getModifierScheme() = listOf(
        ModifierSchemeInfo(
            id = "modifier-scheme-id-1",
            rkId = "rk-modifier-scheme-id-1",
            guid = "modifier-scheme-guid-1",
            code = "modifier-scheme-code-1",
            name = "Схема для стейков",
            status = "ok",
            mainParentIdent = "rk-modifier-scheme-id-x",
            autoOpen = true,
            details = listOf(
                ModifierSchemeInfo.Details(
                    id = "modifier-scheme-detail-id-1",
                    rkId = "rk-modifier-scheme-detail-id-1",
                    guid = "modifier-scheme-detail-guid-1",
                    code = "modifier-scheme-detail-code-1",
                    name = "Деталь схемы для стейков",
                    status = "ok",
                    mainParentIdent = "rk-modifier-scheme-id-1",
                    modifiersGroupRkId = "rk-modifiers-group-id-3",
                    defaultModifier = "rk-modifier-id-medium",
                    upLimit = 1,
                    downLimit = 1,
                    freeCount = true
                )
            )
        )
    )
}














