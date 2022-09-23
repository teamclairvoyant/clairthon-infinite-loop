package com.clairvoyant.iPlanner.Shared.DTO;

import lombok.*;

/**
 * [
 * {
 * "id": "default-event-id-5934302",
 * "title": "simply dummy text of the printing",
 * "start": "2022-09-12",
 * "end": "2022-09-12",
 * "className": "fc-event-primary",
 * "type": {
 * "value": "fc-event-primary",
 * "label": "Company"
 * },
 * "description": "Use a passage of Lorem Ipsum, you need to be sure there isn't anything embarrassing hidden."
 * }
 * ]
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReactCalendarEvent {
    public String id;
    public String title;
    public String start;
    public String end;
    public String className;
    public Type type;
    public String description;
    public Metadata metadata;

    @Data
    public static class Type {
        public String value;
        public String label;
    }

    @Data
    public static class Metadata {
        public String colour;
        public String hangoutsLink;
        public String availability;
    }
}
