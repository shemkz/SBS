from django import template
register = template.Library()


@register.filter(name='underscoreTag')
def underscoreTag(obj, attribute):
    obj = dict(obj)
    return obj.get(attribute)

