export namespace User {

  export class Entity {
    public username: string;
    public canCreateRepo: boolean;
    public email: string;
    public superuser: boolean;
  }

  export class List {
    public users: Entity[];
  }

}
