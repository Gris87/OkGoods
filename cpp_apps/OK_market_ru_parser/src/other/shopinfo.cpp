#include "shopinfo.h"



ShopInfo::ShopInfo()
    : id(0)
    , city_id(0)
    , name("")
    , is_hypermarket(false)
    , latitude(0)
    , longitude(0)
    , phone("")
    , work_hours("")
    , square(0)
    , opening_date()
    , parking_places(0)
    , number_of_cashboxes(0)
    , services_set()
{
}

bool ShopInfo::isLess(const ShopInfo &another) const
{
    return city_id < another.city_id || id < another.id;
}
